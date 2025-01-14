package com.ainsln.core.ui.state

import com.ainsln.core.common.result.AppException
import com.ainsln.core.common.result.Result


sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val e: AppException) : UiState<Nothing>
}

fun <T> Result<T>.toState(): UiState<T> {
    return when(this){
        is Result.Loading -> UiState.Loading
        is Result.Error -> UiState.Error(e)
        is Result.Success -> UiState.Success(data)
    }
}


