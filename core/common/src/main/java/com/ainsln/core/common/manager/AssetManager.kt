package com.ainsln.core.common.manager

import java.io.InputStream

interface AssetManager {
    fun open(filename: String): InputStream
}
