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
import com.app.imagerandom.domain.util.TvShowType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowSeeMoreViewModel @Inject constructor(
    private val getAiringTodayTVShowsUseCase: GetAiringTodayTVShowsUseCase,
    private val getOnTheAirTVShowsUseCase: GetOnTheAirTVShowsUseCase,
    private val getPopularTVShowsUseCase: GetPopularTVShowsUseCase,
    private val getTopRatedTVShowsUseCase: GetTopRatedTVShowsUseCase
) : ViewModel() {

    private val _tvShows = MutableStateFlow<Response<List<TVShow>>>(Response.Success(emptyList()))
    val tvShows = _tvShows.asStateFlow()

    private var currentPage = 1
    private var totalPage = 10
    private var isLoading = false

    private var currentType: String? = null

    fun loadTVShowType(type: String, reset: Boolean = true) {
        if (isLoading) return

        currentType = type

        if (reset) {
            currentPage = 1
            totalPage = 10
            _tvShows.value = Response.Loading()
        } else if (currentPage > totalPage) {
            return
        }

        val useCase = when (type) {
            TvShowType.AIRING_TODAY -> getAiringTodayTVShowsUseCase::getAiringTodayTVShows
            TvShowType.ON_THE_AIR -> getOnTheAirTVShowsUseCase::getOnTheAirTVShows
            TvShowType.POPULAR -> getPopularTVShowsUseCase::getPopularTVShows
            TvShowType.TOP_RATED -> getTopRatedTVShowsUseCase::getTopRatedTVShows
            else -> getTopRatedTVShowsUseCase::getTopRatedTVShows
        }

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                useCase(currentPage).collect { result ->
                    when (result) {
                        is Response.Loading -> if (reset) _tvShows.value = Response.Loading()

                        is Response.Error -> _tvShows.value =
                            Response.Error(result.message ?: "")

                        is Response.Success -> {
                            val response = result.data ?: TVShowResponse(
                                page = currentPage,
                                results = emptyList(),
                                totalPage = totalPage
                            )

                            val oldList = if (reset) emptyList() else _tvShows.value.dataOrNull() ?: emptyList()
                            val merged = oldList + (response.results)

                            _tvShows.value = Response.Success(merged)

                            totalPage = response.totalPage
                            currentPage++
                        }
                    }
                }
            } catch (e: Exception) {
                _tvShows.value = Response.Error(e.localizedMessage ?: "")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadNextPage() {
        val type = currentType ?: return
        if (!isLoading && currentPage <= totalPage) {
            loadTVShowType(type, reset = false)
        }
    }
}