package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.categories.GetMovieListByGenresUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetCreditOfAMovieUseCase
import com.app.imagerandom.domain.usecase.genres.GetMovieGenreListUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetTrailerOfAMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getMovieListByGenresUseCase: GetMovieListByGenresUseCase,
    private val getMovieGenreListUseCase: GetMovieGenreListUseCase,
    private val getCreditOfAMovieUseCase: GetCreditOfAMovieUseCase,
    private val getTrailerOfAMovieUseCase: GetTrailerOfAMovieUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies.asStateFlow()

    private val _movieListForSlideShow = MutableStateFlow<List<MovieItem>>(emptyList())
    val movieListForSlideShow: StateFlow<List<MovieItem>> =
        _movieListForSlideShow.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _credit = MutableStateFlow<MovieCreditsResponse?>(null)
    val credit: StateFlow<MovieCreditsResponse?> = _credit.asStateFlow()

    private val _trailerKey = MutableStateFlow<String?>(null)
    val trailerKey = _trailerKey.asStateFlow()

    private var currentGenres = 10768
    private var currentPage = 1
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadGenresList()
    }

    fun loadMoviesByGenres(genres: Int = 10768, isLoadMore: Boolean = false) {
        if (_isLoading.value) return
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                if (!isLoadMore) {
                    currentGenres = genres
                    currentPage = 1
                    _movies.value = emptyList()
                }

                val result = getMovieListByGenresUseCase.getMovieListByGenres(
                    "vi-VN",
                    currentPage,
                    currentGenres
                )

                if (result.results.isNotEmpty()) {
                    if (isLoadMore) {
                        _movies.value += result.results
                    } else {
                        _movies.value = result.results
                        _movieListForSlideShow.value =
                            result.results.take((result.results.size / 3.0).toInt())
                    }
                    currentPage++
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadGenresList() {
        viewModelScope.launch(Dispatchers.IO) {
            _genres.value = getMovieGenreListUseCase.getAllGenres()
        }
    }

    fun getCreditOfAnMovie(movieId: Int) {
        viewModelScope.launch {
            _credit.value = getCreditOfAMovieUseCase.getCreditOfAnMovie(movieId)
        }
    }

    fun loadTrailer(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = getTrailerOfAMovieUseCase.getTrailerOfAMovie(movieId)
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
