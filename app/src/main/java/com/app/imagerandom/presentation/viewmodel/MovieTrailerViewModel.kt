package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.usecase.movie_detail.GetTrailerOfMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieTrailerViewModel @Inject constructor(
    private val getTrailerOfMovieUseCase: GetTrailerOfMovieUseCase
) : ViewModel() {
    private val _trailerKey = MutableStateFlow<Response<String?>>(Response.Loading())
    val trailerKey = _trailerKey.asStateFlow()

    private var isLoading = false

    fun loadTrailer(movieId: Int) {
        if (isLoading) return

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            getTrailerOfMovieUseCase.getTrailerOfAMovie(movieId).collect { result ->
                _trailerKey.value = when (result) {
                    is Response.Loading -> Response.Loading()
                    is Response.Success -> {
                        val youtubeVideo = result.data?.results?.firstOrNull {
                            it.site.equals("YouTube", ignoreCase = true) && it.type.equals("Trailer", ignoreCase = true)
                        }
                        Response.Success(youtubeVideo?.key)
                    }
                    is Response.Error -> Response.Error(result.message ?: "Lỗi không xác định")
                }
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _trailerKey.value = Response.Success(null)
    }
}