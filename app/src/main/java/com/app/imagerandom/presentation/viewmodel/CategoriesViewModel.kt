package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.app_const.Genres
import com.app.imagerandom.domain.model.GetMovieListResponse
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.categories.CategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies.asStateFlow()

    private val _movieListForSlideShow = MutableStateFlow<List<MovieItem>>(emptyList())
    val movieListForSlideShow: StateFlow<List<MovieItem>> =
        _movieListForSlideShow.asStateFlow()

    private var currentGenres = Genres.MOVIES
    private var currentPage = 1
    private var totalPages = 10
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    fun loadMoviesByGenres(genres: Int = Genres.MOVIES, isLoadMore: Boolean = false) {
        if (_isLoading.value) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (!isLoadMore) {
                    currentGenres = genres
                    currentPage = 1
                    _movies.value = emptyList()
                }

                val result = categoriesUseCase.getMovieListByGenres(
                    "vi-VN",
                    currentPage,
                    currentGenres
                )

                if (result.results.isNotEmpty()) {
                    if (isLoadMore) {
                        _movies.value += result.results
                    } else {
                        _movies.value = result.results
                        _movieListForSlideShow.value = result.results
                        totalPages = result.totalPages
                    }
                    currentPage++
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
