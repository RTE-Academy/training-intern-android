package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.usecase.movie_detail.GetCreditOfMovieUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetMovieDetailUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetTrailerOfMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getCreditOfMovieUseCase: GetCreditOfMovieUseCase,
    private val getTrailerOfMovieUseCase: GetTrailerOfMovieUseCase
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<Response<MovieDetail>>(Response.Loading())
    val movieDetail = _movieDetail.asStateFlow()

    private val _credit = MutableStateFlow<MovieCreditsResponse?>(null)
    val credit: StateFlow<MovieCreditsResponse?> = _credit.asStateFlow()

    private val _trailerKey = MutableStateFlow<String?>(null)
    val trailerKey = _trailerKey.asStateFlow()

    private var isLoading = false

    fun loadMovieDetail(movieId: Int) {
        if (isLoading) return
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            getMovieDetailUseCase.getMovieDetail(movieId).collect { result ->
                _movieDetail.value = when (result) {
                    is Response.Loading -> Response.Loading()
                    is Response.Success -> Response.Success(result.data ?: MovieDetail())
                    is Response.Error -> Response.Error(result.message ?: "Lỗi không xác định")
                }
                _credit.value = getCreditOfMovieUseCase.getCreditOfMovie(movieId)
                isLoading = false
            }
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