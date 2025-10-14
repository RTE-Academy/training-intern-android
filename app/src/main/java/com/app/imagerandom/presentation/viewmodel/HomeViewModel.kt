package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.movie_detail.GetCreditOfAMovieUseCase
import com.app.imagerandom.domain.usecase.genres.GetMovieGenreListUseCase
import com.app.imagerandom.domain.usecase.home.HomeUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetTrailerOfAMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
    private val homeUseCase: HomeUseCase,
    private val getMovieGenreListUseCase: GetMovieGenreListUseCase,
    private val getCreditOfAMovieUseCase: GetCreditOfAMovieUseCase,
    private val getTrailerOfAMovieUseCase: GetTrailerOfAMovieUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _credit = MutableStateFlow<MovieCreditsResponse?>(null)
    val credit: StateFlow<MovieCreditsResponse?> = _credit.asStateFlow()

    private val _trailerKey = MutableStateFlow<String?>(null)
    val trailerKey = _trailerKey.asStateFlow()

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE
    var isLoading = false

    init {
        loadMovies()
        loadGenresList()
    }

    fun checkAutoSignIn(): Boolean {
        return !sharedPrefHelper.getSessionId().isNullOrEmpty()
    }

    fun loadMovies() {
        if (isLoading || currentPage > totalPages) return

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val response = homeUseCase.getMovieList("vi-Vietnam", currentPage)
                if (response.results.isNotEmpty()) {
                    _movies.value += response.results
                    currentPage++
                    totalPages = response.totalPages
                }
            } catch (_: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadGenresList() {
        viewModelScope.launch(Dispatchers.IO) {
            _genres.value = getMovieGenreListUseCase.getAllGenres()
        }
    }

    fun getCreditOfAMovie(movieId: Int) {
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
