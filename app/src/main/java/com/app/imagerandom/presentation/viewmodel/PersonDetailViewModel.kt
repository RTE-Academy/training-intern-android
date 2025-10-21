package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.domain.model.PersonDetail
import com.app.imagerandom.domain.usecase.people.GetPersonDetailByIdUseCase
import com.app.imagerandom.domain.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val getPersonDetailByIdUseCase: GetPersonDetailByIdUseCase
) : ViewModel() {

    private val _personDetail = MutableStateFlow<Response<PersonDetail>>(Response.Loading())
    val personDetail = _personDetail.asStateFlow()

    var isLoading = false
        private set

    fun loadPersonDetail(id: Int) {
        if (isLoading) return
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                getPersonDetailByIdUseCase.getPersonDetailById(id).collect { result ->
                    when (result) {
                        is Response.Loading -> {
                            _personDetail.value = Response.Loading()
                        }
                        is Response.Error -> {
                            _personDetail.value = Response.Error(result.message ?: "Lỗi không xác định")
                        }
                        is Response.Success -> {
                            _personDetail.value = Response.Success(result.data ?: PersonDetail())
                        }
                    }
                }
            } catch (e: Exception) {
                _personDetail.value = Response.Error(e.localizedMessage ?: "Lỗi khi tải chi tiết diễn viên")
            } finally {
                isLoading = false
            }
        }
    }
}