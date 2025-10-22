package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.domain.usecase.person.GetPersonUseCase
import com.app.imagerandom.domain.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val getPersonUseCase: GetPersonUseCase
) : ViewModel() {

    private val _person = MutableStateFlow<Response<PersonResponse>>(
        Response.Success(
            PersonResponse(
                page = 1,
                results = emptyList(),
                totalPage = 10
            )
        )
    )
    val person = _person.asStateFlow()

    private var currentPage = 1
    var isLoading = false
    private var totalPage = 10

    init {
        loadPeople(reset = true)
    }

    private fun loadPeople(language: String = "vi-VN", reset: Boolean = false) {
        if (isLoading) return
        if (reset) {
            currentPage = 1
            totalPage = 10
            _person.value = Response.Loading()
        } else {
            if (currentPage > totalPage) return
        }

        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                getPersonUseCase.getPerson(language, currentPage).collect { result ->
                    when (result) {
                        is Response.Loading -> {
                            if (reset) _person.value = Response.Loading()
                        }

                        is Response.Error -> {
                            _person.value = Response.Error(result.message ?: "Lỗi không xác định")
                        }

                        is Response.Success -> {
                            val incoming = result.data ?: PersonResponse(
                                page = currentPage,
                                results = emptyList(),
                                totalPage = 10
                            )
                            val oldList = _person.value.dataOrNull()?.results ?: emptyList()

                            val merged = if (reset) incoming.results else oldList + incoming.results

                            _person.value = Response.Success(
                                PersonResponse(
                                    page = incoming.page.takeIf { it > 0 } ?: currentPage,
                                    results = merged,
                                    totalPage = incoming.totalPage
                                )
                            )

                            totalPage = incoming.totalPage
                            currentPage++
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

    fun loadNextPage() {
        if (!isLoading && currentPage < totalPage) {
            loadPeople(reset = false)
        }
    }
}