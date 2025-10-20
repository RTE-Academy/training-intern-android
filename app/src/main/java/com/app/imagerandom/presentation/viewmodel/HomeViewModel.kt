package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.movie_detail.GetCreditOfMovieUseCase
import com.app.imagerandom.domain.usecase.genres.GetMovieGenreListUseCase
import com.app.imagerandom.domain.usecase.home.HomeUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetTrailerOfMovieUseCase
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
    private val getMovieGenreListUseCase: GetMovieGenreListUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

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
}
