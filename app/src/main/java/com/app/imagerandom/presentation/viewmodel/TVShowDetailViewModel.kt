package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShowDetail
import com.app.imagerandom.domain.usecase.tv_show.GetTVShowDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TVShowDetailViewModel @Inject constructor(
    private val getTVShowDetailUseCase: GetTVShowDetailUseCase
) : ViewModel() {

    private val _tvShowDetail = MutableStateFlow<Response<TVShowDetail>>(Response.Success(TVShowDetail()))
    val tvShowDetail = _tvShowDetail.asStateFlow()

    private var isLoading = false

    fun loadTVShowDetail(seriesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            getTVShowDetailUseCase.getTVShowDetail(seriesId).collect { result ->
                when (result) {
                    is Response.Success -> {
                        _tvShowDetail.value = result
                    }
                    is Response.Error -> {
                        _tvShowDetail.value = Response.Error(result.message ?: "Lỗi không xác định")
                    }
                    is Response.Loading -> {
                        _tvShowDetail.value = Response.Loading()
                    }
                }
                isLoading = false
            }
        }
    }
}