package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.MovieSearchResult
import com.app.imagerandom.domain.usecase.search.SearchMovieUseCase
import com.app.imagerandom.domain.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
) : ViewModel() {

    private val _searchResults =
        MutableStateFlow<Response<List<MovieSearchResult>>>(Response.Success(emptyList()))
    val searchResults = _searchResults.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    private var currentPage = 1
    private var totalPages = 5
    private var isLoading = false
    private var lastQuery = ""

    fun searchMovies(reset: Boolean = false) {
        val query = _currentQuery.value.trim()

        if (query.isEmpty()) {
            _searchResults.value = Response.Success(emptyList())
            return
        }

        if (reset || query != lastQuery) {
            lastQuery = query
            currentPage = 1
            totalPages = 5
            _searchResults.value = Response.Loading()
        }

        if (isLoading || currentPage > totalPages) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            searchMovieUseCase.searchMovie(query, currentPage).collect { result ->
                when (result) {
                    is Response.Success -> {
                        val oldData = if (reset) emptyList() else (_searchResults.value.dataOrNull() ?: emptyList())
                        val newData = result.data ?: emptyList()
                        _searchResults.value = Response.Success(oldData + newData)
                        currentPage++
                        totalPages = newData.firstOrNull()?.totalPage ?: totalPages
                    }
                    is Response.Error -> {
                        _searchResults.value = Response.Error(result.message ?: "Lỗi không xác định")
                    }
                    is Response.Loading -> {
                        if (reset) _searchResults.value = Response.Loading()
                    }
                }
                isLoading = false
            }
        }
    }

    fun loadNextPage() {
        searchMovies()
    }

    fun onUpdateQuery(newQuery: String) {
        _currentQuery.value = newQuery
    }

    fun onClearQuery() {
        _currentQuery.value = ""
        lastQuery = ""
        currentPage = 1
        totalPages = 5
        _searchResults.value = Response.Success(emptyList())
    }
}