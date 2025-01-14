package com.ainsln.core.data.utils

import android.database.sqlite.SQLiteException
import com.ainsln.core.common.utils.Logger
import com.ainsln.core.common.result.AppException
import jakarta.inject.Inject
import retrofit2.HttpException
import java.io.IOException

interface ExceptionHandler {
    fun handle(e: Throwable): AppException
}

class BaseExceptionHandler @Inject constructor(
    private val logger: Logger
) : ExceptionHandler {
    override fun handle(e: Throwable): AppException {
        return when(e){
            is HttpException -> handleHttpException(e)
            is SQLiteException -> {
                logger.e(LOG_TAG, "SQLiteException. Message: ${e.message}")
                AppException.DatabaseError(e.message)
            }
            is IOException -> {
                logger.e(LOG_TAG, "IOException. Message: ${e.message}")
                AppException.NetworkError(code = 0, msg = "Network error occurred")
            }
            else -> {
                logger.e(LOG_TAG, "UnknownError. Message: ${e.message}")
                AppException.UnknownError(e.message)
            }
        }
    }

    private fun handleHttpException(e: HttpException): AppException {
        logger.e(LOG_TAG, "HttpException. Code: ${e.code()} Message: ${e.response()?.errorBody()?.string()}")
        return when(e.code()){
            404 -> AppException.NotFound
            429 -> AppException.TooManyRequests
            else -> AppException.NetworkError(e.code(), e.message)
        }
    }

    companion object {
        private const val LOG_TAG = "BaseExceptionHandler"
    }
}
