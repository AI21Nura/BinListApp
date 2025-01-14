package com.ainsln.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.ainsln.core.ui.theme.ClickableTextColor

enum class FeatureLabelLevel { UPPER, LOWER }

@Composable
fun ClickableFeatureText(
    label: String,
    clickableText: String,
    onTextClick: LinkInteractionListener,
    linkTag: String = "",
    modifier: Modifier = Modifier
){
    Feature(
        label = label,
        level = FeatureLabelLevel.LOWER
    ) {
        Text(
            buildAnnotatedString {
                val link = LinkAnnotation.Clickable(
                    tag = linkTag,
                    styles = TextLinkStyles(SpanStyle(color = ClickableTextColor)),
                    linkInteractionListener = onTextClick)
                withLink(link) { append(clickableText) }
            },
            textAlign = TextAlign.Justify,
            modifier = modifier
        )
    }
}

@Composable
fun Feature(
    label: String,
    value: String?,
    modifier: Modifier = Modifier,
    level: FeatureLabelLevel = FeatureLabelLevel.UPPER
){
    Feature(label, modifier, level) {
        Text(
            text = value ?: "N/A",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(0.dp, 150.dp)
        )
    }
}

@Composable
fun Feature(
    label: String,
    modifier: Modifier = Modifier,
    level: FeatureLabelLevel = FeatureLabelLevel.UPPER,
    content: @Composable () -> Unit
){
    val textStyle = if (level == FeatureLabelLevel.LOWER)
        MaterialTheme.typography.labelLarge
    else
        MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outlineVariant
        )

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            style = textStyle,
            color = MaterialTheme.colorScheme.outline
        )
        content()
    }
}
