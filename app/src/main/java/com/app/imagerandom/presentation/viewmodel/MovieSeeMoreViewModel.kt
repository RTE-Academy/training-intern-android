package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.MovieItem
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.usecase.movie.GetMovieListNowPlayingUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListPopularUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListTopRatedUseCase
import com.app.imagerandom.domain.usecase.movie.GetMovieListUpcomingUseCase
import com.app.imagerandom.domain.util.MovieType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSeeMoreViewModel @Inject constructor(
    private val getMovieListNowPlayingUseCase: GetMovieListNowPlayingUseCase,
    private val getMovieListPopularUseCase: GetMovieListPopularUseCase,
    private val getMovieListTopRatedUseCase: GetMovieListTopRatedUseCase,
    private val getMovieListUpcomingUseCase: GetMovieListUpcomingUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<Response<List<MovieItem>>>(Response.Success(emptyList()))
    val movies = _movies.asStateFlow()

    private var currentPage = 1
    private var totalPage = 10
    private var isLoading = false

    private var currentType: String? = null

    fun loadMovieType(type: String, reset: Boolean = true) {
        if (isLoading) return

        currentType = type

        if (reset) {
            currentPage = 1
            totalPage = 10
            _movies.value = Response.Loading()
        } else if (currentPage > totalPage) {
            return
        }

        val useCase = when (type) {
            MovieType.NOW_PLAYING -> getMovieListNowPlayingUseCase::getMovieListNowPlaying
            MovieType.POPULAR -> getMovieListPopularUseCase::getMovieListPopular
            MovieType.TOP_RATED -> getMovieListTopRatedUseCase::getMovieListTopRated
            MovieType.UPCOMING -> getMovieListUpcomingUseCase::getMovieListUpcoming
            else -> getMovieListNowPlayingUseCase::getMovieListNowPlaying
        }

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val language = "vi-VN"
                val movieResponse = useCase(language, currentPage)

                val oldList = if (reset) emptyList() else _movies.value.dataOrNull() ?: emptyList()
                val merged = oldList + (movieResponse.results)

                _movies.value = Response.Success(merged)

                totalPage = movieResponse.totalPages
                currentPage++
            } catch (e: Exception) {
                _movies.value = Response.Error(e.localizedMessage ?: "")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadNextPage() {
        val type = currentType ?: return
        if (!isLoading && currentPage <= totalPage) {
            loadMovieType(type, reset = false)
        }
    }
}