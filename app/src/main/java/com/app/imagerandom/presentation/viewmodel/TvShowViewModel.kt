package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.model.TVShow
import com.app.imagerandom.domain.model.TVShowResponse
import com.app.imagerandom.domain.usecase.tv_show.GetAiringTodayTVShowsUseCase
import com.app.imagerandom.domain.usecase.tv_show.GetOnTheAirTVShowsUseCase
import com.app.imagerandom.domain.usecase.tv_show.GetPopularTVShowsUseCase
import com.app.imagerandom.domain.usecase.tv_show.GetTopRatedTVShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowViewModel @Inject constructor(
    private val getAiringTodayTVShowsUseCase: GetAiringTodayTVShowsUseCase,
    private val getOnTheAirTVShowsUseCase: GetOnTheAirTVShowsUseCase,
    private val getPopularTVShowsUseCase: GetPopularTVShowsUseCase,
    private val getTopRatedTVShowsUseCase: GetTopRatedTVShowsUseCase
) : ViewModel() {

    private val _airingToday = MutableStateFlow<Response<List<TVShow>>>(Response.Success(emptyList()))
    val airingToday = _airingToday.asStateFlow()

    private val _onTheAir = MutableStateFlow<Response<List<TVShow>>>(Response.Success(emptyList()))
    val onTheAir = _onTheAir.asStateFlow()

    private val _popular = MutableStateFlow<Response<List<TVShow>>>(Response.Success(emptyList()))
    val popular = _popular.asStateFlow()

    private val _topRated = MutableStateFlow<Response<List<TVShow>>>(Response.Success(emptyList()))
    val topRated = _topRated.asStateFlow()

    private var isAiringTodayLoading = false
    private var isOnTheAirLoading = false
    private var isPopularLoading = false
    private var isTopRatedLoading = false

    init {
        loadAiringToday()
        loadOnTheAir()
        loadPopular()
        loadTopRated()
    }

    private fun loadAiringToday() {
        if (isAiringTodayLoading) return
        loadTVShows(
            useCase = getAiringTodayTVShowsUseCase::getAiringTodayTVShows,
            state = _airingToday,
            isLoading = { isAiringTodayLoading = it }
        )
    }

    private fun loadOnTheAir() {
        if (isOnTheAirLoading) return
        loadTVShows(
            useCase = getOnTheAirTVShowsUseCase::getOnTheAirTVShows,
            state = _onTheAir,
            isLoading = { isOnTheAirLoading = it }
        )
    }

    private fun loadPopular() {
        if (isPopularLoading) return
        loadTVShows(
            useCase = getPopularTVShowsUseCase::getPopularTVShows,
            state = _popular,
            isLoading = { isPopularLoading = it }
        )
    }

    private fun loadTopRated() {
        if (isTopRatedLoading) return
        loadTVShows(
            useCase = getTopRatedTVShowsUseCase::getTopRatedTVShows,
            state = _topRated,
            isLoading = { isTopRatedLoading = it }
        )
    }

    private fun loadTVShows(
        useCase: suspend (Int) -> Flow<Response<TVShowResponse>>,
        state: MutableStateFlow<Response<List<TVShow>>>,
        isLoading: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading(true)
            useCase(1).collect { result ->
                when (result) {
                    is Response.Success -> {
                        state.value = Response.Success(result.data?.results ?: emptyList())
                    }
                    is Response.Error -> {
                        state.value = Response.Error(result.message ?: "Lỗi không xác định")
                    }
                    is Response.Loading -> {
                        state.value = Response.Loading()
                    }
                }
                isLoading(false)
            }
        }
    }
}