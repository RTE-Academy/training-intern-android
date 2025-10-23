package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.domain.model.Response
import com.app.imagerandom.domain.usecase.person.GetPersonUseCase
import com.app.imagerandom.domain.usecase.person.SearchPersonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val getPersonUseCase: GetPersonUseCase,
    private val searchPersonUseCase: SearchPersonUseCase,
) : ViewModel() {

    private val _person =
        MutableStateFlow<Response<PersonResponse>>(
            Response.Success(
                PersonResponse(
                    1,
                    emptyList(),
                    10
                )
            )
        )
    val person: StateFlow<Response<PersonResponse>> = _person.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    private var currentPage = 1
    private var totalPages = 10
    var isLoading = false
    private var lastQuery = ""

    init {
        loadPerson(reset = true)
    }

    private fun fetchData(
        reset: Boolean,
        request: suspend (Int) -> Flow<Response<PersonResponse>>,
    ) {
        if (isLoading || currentPage > totalPages) return

        viewModelScope.launch(Dispatchers.IO) {
            if (reset) {
                _person.value = Response.Loading()
                currentPage = 1
                totalPages = 10
            }

            isLoading = true
            try {
                request(currentPage).collect { result ->
                    when (result) {
                        is Response.Success -> {
                            val newData =
                                result.data ?: PersonResponse(currentPage, emptyList(), totalPages)
                            val oldList =
                                if (reset) emptyList() else _person.value.dataOrNull()?.results
                                    ?: emptyList()
                            val mergedList = oldList + newData.results

                            _person.value = Response.Success(
                                PersonResponse(
                                    page = newData.page.takeIf { it > 0 } ?: currentPage,
                                    results = mergedList,
                                    totalPage = newData.totalPage
                                )
                            )

                            currentPage++
                            totalPages = newData.totalPage
                        }

                        is Response.Error -> {
                            _person.value = Response.Error(result.message ?: "Lỗi không xác định")
                        }

                        is Response.Loading -> {
                            if (reset) _person.value = Response.Loading()
                        }
                    }
                }
            } catch (e: Exception) {
                _person.value = Response.Error(e.localizedMessage ?: "Lỗi không xác định")
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadPerson(reset: Boolean = false, language: String = "vi-VN") {
        fetchData(reset) { page ->
            getPersonUseCase.getPerson(language, page)
        }
    }

    fun searchPerson(reset: Boolean = false) {
        val query = _currentQuery.value

        if (query.isEmpty()) {
            loadPerson(reset = true)
            return
        }

        if (reset || query != lastQuery) {
            lastQuery = query
            currentPage = 1
            totalPages = 10
            _person.value = Response.Loading()
        }

        fetchData(reset) { page ->
            searchPersonUseCase.searchPerson(query, page)
        }
    }

    fun loadNextPage() {
        if (!isLoading && currentPage <= totalPages) {
            if (_currentQuery.value.isNotBlank()) {
                searchPerson(reset = false)
            } else {
                loadPerson(reset = false)
            }
        }
    }

    fun updateQuery(newQuery: String) {
        _currentQuery.value = newQuery
    }

    fun clearQuery() {
        if (_currentQuery.value.isNotBlank()) {
            _currentQuery.value = ""
            lastQuery = ""
            currentPage = 1
            totalPages = 10
            loadPerson(reset = true)
        }
    }
}