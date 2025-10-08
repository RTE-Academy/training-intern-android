package com.app.imagerandom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.imagerandom.data.local.SharedPrefHelper
import com.app.imagerandom.domain.model.ApiErrorResponse
import com.app.imagerandom.domain.usecase.auth.StartAuthFlowUseCase
import com.app.imagerandom.domain.usecase.genres.FetchMovieGenreListUseCase
import com.app.imagerandom.domain.usecase.genres.SaveMovieGenreListUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val startAuthFlowUseCase: StartAuthFlowUseCase,
    private val saveMovieGenreListUseCase: SaveMovieGenreListUseCase,
    private val fetchMovieGenreListUseCase: FetchMovieGenreListUseCase,
    private val sharedPrefHelper: SharedPrefHelper
) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun startAuthFlow(username: String, password: String, navigateToHomeScreen: () -> Unit) {
        viewModelScope.launch {
            try {
                val result = startAuthFlowUseCase.startAuthFlow(username, password)

                if (result.isNotEmpty()) {
                    // Create session
                    sharedPrefHelper.saveSessionId(result)
                    loadGenresFromApi()
                    navigateToHomeScreen()
                } else {
                    _error.value = "Token không hợp lệ"
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        if (!errorBody.isNullOrEmpty()) {
                            try {
                                val errorResponse = Gson().fromJson(errorBody, ApiErrorResponse::class.java)
                                errorResponse.statusMessage ?: "Unknown API error"
                            } catch (ex: Exception) {
                                "Error parsing error response"
                            }
                        } else {
                            "Unknown API error"
                        }
                    }
                    else -> e.message ?: "Unknown error"
                }
                _error.value = errorMessage
            }
        }
    }

    private fun loadGenresFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = fetchMovieGenreListUseCase.fetchMovieGenreList()
            val list = response.genres
            saveMovieGenreListUseCase.saveMovieGenreList(list)
        }
    }
}
