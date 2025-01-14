package com.ainsln.core.common.utils

import android.util.Log
import javax.inject.Inject

interface Logger {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String)
}

class AndroidLogcatLogger @Inject constructor() : Logger {
    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}
