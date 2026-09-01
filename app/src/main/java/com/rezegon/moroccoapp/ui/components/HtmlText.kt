package com.rezegon.moroccoapp.ui.components

import android.graphics.Typeface
import android.text.Html
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    val spanned = Html.fromHtml(
        text,
        Html.FROM_HTML_MODE_LEGACY
    )

    val spans = spanned.getSpans(
        0,
        spanned.length,
        StyleSpan::class.java
    )

    val sizeSpans = spanned.getSpans(
        0,
        spanned.length,
        RelativeSizeSpan::class.java
    )

    val annotatedString = buildAnnotatedString {
        append(spanned.toString())

        spans.forEach { span ->

            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)

            when (span.style) {

                Typeface.BOLD -> {
                    addStyle(
                        SpanStyle(fontWeight = FontWeight.Bold),
                        start,
                        end
                    )
                }

                Typeface.ITALIC -> {
                    addStyle(
                        SpanStyle(fontStyle = FontStyle.Italic),
                        start,
                        end
                    )
                }

                Typeface.BOLD_ITALIC -> {
                    addStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        ),
                        start,
                        end
                    )
                }
            }
        }


        sizeSpans.forEach { span ->

            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)

            addStyle(
                SpanStyle(
                    fontSize = (16 * span.sizeChange).sp
                ),
                start,
                end
            )
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        textAlign = textAlign
    )
}