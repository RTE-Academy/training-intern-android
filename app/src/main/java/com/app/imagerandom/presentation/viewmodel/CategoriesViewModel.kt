package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.Genre
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.categories.GetMovieListByGenresUseCase
import com.app.imagerandom.domain.usecase.genres.GetMovieGenreListUseCase
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
    private val getMovieGenreListUseCase: GetMovieGenreListUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies.asStateFlow()

    private val _movieListForSlideShow = MutableStateFlow<List<MovieItem>>(emptyList())
    val movieListForSlideShow: StateFlow<List<MovieItem>> =
        _movieListForSlideShow.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

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
                        _movieListForSlideShow.value = result.results.take((result.results.size / 3.0).toInt())
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
}
