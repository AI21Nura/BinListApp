package com.ainsln.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ClickableText = Color(0xFF4167FF)

val ClickableTextColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color.LightGray else ClickableText
