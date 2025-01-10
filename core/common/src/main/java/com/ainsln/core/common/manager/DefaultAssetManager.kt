package com.ainsln.core.common.manager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject

class DefaultAssetManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AssetManager {
    override fun open(filename: String): InputStream =
        context.assets.open(filename)
}
