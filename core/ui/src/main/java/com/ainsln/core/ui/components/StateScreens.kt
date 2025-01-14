package com.ainsln.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ainsln.core.common.result.AppException
import com.ainsln.core.ui.R

@Composable
fun InfoBlockPlaceholder(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
    ) {
        content()
    }
}

@Composable
fun AppPlaceholder(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    InfoBlockPlaceholder(modifier){
        Icon(
            painter = icon,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(112.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = 28.dp)
        )
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    InfoBlockPlaceholder(modifier) {
        CircularProgressIndicator(
            Modifier
                .size(64.dp)
                .testTag("loadingIndicator")
        )
    }
}

@Composable
fun ErrorScreen(
    e: AppException,
    icon: Painter,
    modifier: Modifier = Modifier
){
    val msg = when(e){
        is AppException.NotFound -> stringResource(R.string.error_not_found)
        is AppException.TooManyRequests -> stringResource(R.string.error_too_many_requests)
        is AppException.NetworkError -> stringResource(R.string.error_network)
        is AppException.DatabaseError -> stringResource(R.string.error_database)
        is AppException.UnknownError -> stringResource(R.string.error_unknown)
    }

    AppPlaceholder(
        text = msg,
        icon = icon,
        modifier = modifier
    )
}
