package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.usecase.home.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPrefHelper: SharedPrefHelper,
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<MovieItem>>(emptyList())
    val movies: StateFlow<List<MovieItem>> = _movies.asStateFlow()
    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE
    var isLoading = false

    init {
        loadMovies()
    }

    fun checkAutoSignIn(): Boolean {
        return !sharedPrefHelper.getSessionId().isNullOrEmpty()
    }

    fun loadMovies() {
        if (isLoading || currentPage > totalPages) return

        viewModelScope.launch {
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
}
