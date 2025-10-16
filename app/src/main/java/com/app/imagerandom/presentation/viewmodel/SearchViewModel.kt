package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieSearchResult
import com.app.imagerandom.domain.usecase.movie_detail.GetCreditOfMovieUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetTrailerOfMovieUseCase
import com.app.imagerandom.domain.usecase.search.SearchMovieUseCase
import com.app.imagerandom.util.Resource
import com.app.imagerandom.util.extensions.dataOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
    private val getCreditOfMovieUseCase: GetCreditOfMovieUseCase,
    private val getTrailerOfMovieUseCase: GetTrailerOfMovieUseCase
) : ViewModel() {

    private val _searchResults =
        MutableStateFlow<Resource<List<MovieSearchResult>>>(Resource.Success(emptyList()))
    val searchResults = _searchResults.asStateFlow()

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE
    private var currentQuery = ""
    private var isLoading = false

    private val _credit = MutableStateFlow<MovieCreditsResponse?>(null)
    val credit: StateFlow<MovieCreditsResponse?> = _credit.asStateFlow()

    private val _trailerKey = MutableStateFlow<String?>(null)
    val trailerKey = _trailerKey.asStateFlow()

    init {
        searchMovies("A", reset = true)
    }

    fun searchMovies(query: String, reset: Boolean = false) {
        if (reset || query != currentQuery) {
            currentQuery = query
            currentPage = 1
            totalPages = Int.MAX_VALUE
            _searchResults.value = Resource.Loading()
        }

        if (isLoading || currentPage > totalPages) return

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            searchMovieUseCase.searchMovie(query, currentPage).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val oldData = if (reset) emptyList() else (_searchResults.value.dataOrNull()
                            ?: emptyList())
                        val newData = result.data ?: emptyList()

                        _searchResults.value = Resource.Success(oldData + newData)

                        if (newData.isNotEmpty()) {
                            currentPage++
                        } else {
                            totalPages = currentPage
                        }
                    }

                    is Resource.Error -> {
                        _searchResults.value =
                            Resource.Error(result.message ?: "Lỗi không xác định")
                    }

                    is Resource.Loading -> {
                        if (reset) _searchResults.value = Resource.Loading()
                    }
                }
                isLoading = false
            }
        }
    }

    fun loadNextPage() = searchMovies(currentQuery)

    fun getCreditOfAMovie(movieId: Int) {
        viewModelScope.launch {
            _credit.value = getCreditOfMovieUseCase.getCreditOfMovie(movieId)
        }
    }

    fun loadTrailer(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getTrailerOfMovieUseCase.getTrailerOfAMovie(movieId)
                val youtubeVideo = response.results.firstOrNull {
                    it.site.equals("YouTube", true) && it.type.equals("Trailer", true)
                }
                _trailerKey.value = youtubeVideo?.key
            } catch (e: Exception) {
                e.printStackTrace()
                _trailerKey.value = null
            }
        }
    }

    fun clearTrailerKey() {
        _trailerKey.value = null
    }
}