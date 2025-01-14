package com.ainsln.feature.bincards.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

internal class BinVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val input = text.text
        val formatted = buildString {
            for (i in input.indices) {
                if (i == 4) append(" ")
                append(input[i])
            }
        }
        return TransformedText(
            AnnotatedString(formatted),
            BinOffsetMapping(input.length)
        )
    }

    private class BinOffsetMapping(private val contentLength: Int) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 0) return 0
            if (offset > contentLength) return contentLength + (if (contentLength > 4) 1 else 0)

            return when {
                offset <= 4 -> offset
                else -> offset + 1
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 0) return 0

            val maxTransformedLength = contentLength + (if (contentLength > 4) 1 else 0)
            if (offset > maxTransformedLength) return contentLength

            return when {
                offset <= 4 -> offset
                else -> offset - 1
            }
        }
    }
}


