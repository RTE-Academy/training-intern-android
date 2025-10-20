package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.MovieCreditsResponse
import com.app.imagerandom.domain.model.MovieDetail
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.usecase.movie_detail.GetCreditOfMovieUseCase
import com.app.imagerandom.domain.usecase.movie_detail.GetMovieDetailUseCase
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
    private val getCreditOfMovieUseCase: GetCreditOfMovieUseCase
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<Response<MovieDetail>>(Response.Loading())
    val movieDetail = _movieDetail.asStateFlow()

    private val _credit = MutableStateFlow<MovieCreditsResponse?>(null)
    val credit: StateFlow<MovieCreditsResponse?> = _credit.asStateFlow()

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
}