package com.app.imagerandom.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.domain.model.GetMovieListResponse
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
    var movies = mutableStateListOf<MovieItem>()

    private var currentPage = 1
    private var totalPages = Int.MAX_VALUE

    init {
        viewModelScope.launch {
            val result = getMovieList(page = currentPage)
            _pageAndListMovie.value = result
            movies.addAll(result.results)
            currentPage++
        }
    }

    fun checkAutoSignIn(): Boolean {
        return !sharedPrefHelper.getSessionId().isNullOrEmpty()
    }

    // Get movie list
    private val _pageAndListMovie = MutableStateFlow<GetMovieListResponse?>(null)
    val pageAndListMovie: StateFlow<GetMovieListResponse?> = _pageAndListMovie.asStateFlow()

    private suspend fun getMovieList(
        page: Int = 1,
        language: String = "vi-Vietnam"
    ): GetMovieListResponse {
        return homeUseCase.getMovieList(language, page)
    }

    // Load more movies
    private var isLoading = false
    fun loadMoreMovies() {
        if (isLoading || currentPage > totalPages) {
            return
        }
        viewModelScope.launch {
            isLoading = true
            try {
                val response = homeUseCase.getMovieList("vi-Vietnam", currentPage)
                if (response.results.isNotEmpty()) {
                    movies.addAll(response.results)
                    _pageAndListMovie.value = response
                    currentPage++
                }
            } catch (_: Exception) { }
            finally {
                isLoading = false
            }
        }
    }
}