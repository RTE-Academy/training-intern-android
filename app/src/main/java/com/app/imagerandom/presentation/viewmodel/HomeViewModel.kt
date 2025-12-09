package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.genres.GetMovieGenreListUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListPopularUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListNowPlayingUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListTopRatedUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListUpcomingUseCase
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
    private val getMovieListPopularUseCase: GetMovieListPopularUseCase,
    private val getMovieListNowPlayingUseCase: GetMovieListNowPlayingUseCase,
    private val getMovieListTopRatedUseCase: GetMovieListTopRatedUseCase,
    private val getMovieListUpcomingUseCase: GetMovieListUpcomingUseCase,
    private val getMovieGenreListUseCase: GetMovieGenreListUseCase
) : ViewModel() {

    private val _popular = MutableStateFlow<List<MovieItem>>(emptyList())
    val popular: StateFlow<List<MovieItem>> = _popular.asStateFlow()

    private val _nowPlaying = MutableStateFlow<List<MovieItem>>(emptyList())
    val nowPlaying: StateFlow<List<MovieItem>> = _nowPlaying.asStateFlow()

    private val _topRated = MutableStateFlow<List<MovieItem>>(emptyList())
    val topRated: StateFlow<List<MovieItem>> = _topRated.asStateFlow()

    private val _upcoming = MutableStateFlow<List<MovieItem>>(emptyList())
    val upcoming: StateFlow<List<MovieItem>> = _upcoming.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private var isPopularLoading = false
    private var isNowPlayingLoading = false
    private var isTopRatedLoading = false
    private var isUpcomingLoading = false

    init {
        loadPopular()
        loadNowPlaying()
        loadTopRated()
        loadUpcoming()
        loadGenresList()
    }

    fun checkAutoSignIn(): Boolean {
        return !sharedPrefHelper.getSessionId().isNullOrEmpty()
    }

    private fun loadMovies(
        useCase: suspend (String, Int) -> GetMovieListResponse,
        state: MutableStateFlow<List<MovieItem>>,
        isLoading: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading(true)
            try {
                val response = useCase("vi-Vietnam", 1)
                state.value = response.results
            } catch (_: Exception) {
                state.value = emptyList()
            } finally {
                isLoading(false)
            }
        }
    }

    private fun loadPopular() {
        if (isPopularLoading) return
        loadMovies(
            useCase = getMovieListPopularUseCase::getMovieListPopular,
            state = _popular,
            isLoading = { isPopularLoading = it }
        )
    }

    private fun loadNowPlaying() {
        if (isNowPlayingLoading) return
        loadMovies(
            useCase = getMovieListNowPlayingUseCase::getMovieListNowPlaying,
            state = _nowPlaying,
            isLoading = { isNowPlayingLoading = it }
        )
    }

    private fun loadTopRated() {
        if (isTopRatedLoading) return
        loadMovies(
            useCase = getMovieListTopRatedUseCase::getMovieListTopRated,
            state = _topRated,
            isLoading = { isTopRatedLoading = it }
        )
    }

    private fun loadUpcoming() {
        if (isUpcomingLoading) return
        loadMovies(
            useCase = getMovieListUpcomingUseCase::getMovieListUpcoming,
            state = _upcoming,
            isLoading = { isUpcomingLoading = it }
        )
    }

    private fun loadGenresList() {
        viewModelScope.launch(Dispatchers.IO) {
            _genres.value = getMovieGenreListUseCase.getAllGenres()
        }
    }
}