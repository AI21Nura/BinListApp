package com.ainsln.core.data.utils

import jakarta.inject.Inject
import java.util.Date

fun interface DateTimeProvider {
    fun getCurrentDate(): Date
}

class SystemDateTimeProvider @Inject constructor() : DateTimeProvider {
    override fun getCurrentDate(): Date = Date(System.currentTimeMillis())
}
