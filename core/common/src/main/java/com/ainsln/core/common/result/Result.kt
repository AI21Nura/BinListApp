package com.ainsln.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data object Loading : Result<Nothing>
    data class Error(val e: AppException) : Result<Nothing>
}

fun <T> Flow<T>.asFlowResult(
    handleException: (Throwable) -> AppException
): Flow<Result<T>> {
    return map<T, Result<T>> { data -> Result.Success(data)}
        .onStart { emit(Result.Loading) }
        .catch { e -> emit(Result.Error(handleException(e))) }
}
