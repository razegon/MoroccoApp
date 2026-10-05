package com.rezegon.moroccoapp.ui.components

import androidx.compose.runtime.Composable
import com.rezegon.shared.domain.model.DescriptionBlock
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DescriptionContent(
    blocks: List<DescriptionBlock>
) {
    Column {
        blocks.forEach { block ->
            when (block) {
                is DescriptionBlock.Paragraph -> {
                    Text(
                        text = buildAnnotatedString {
                            block.parts.forEach { part ->
                                withStyle(
                                    SpanStyle(
                                        fontWeight = if (part.bold) {
                                            FontWeight.Bold
                                        } else {
                                            FontWeight.Normal
                                        },
                                        fontStyle = if (part.italic) {
                                            FontStyle.Italic
                                        } else {
                                            FontStyle.Normal
                                        }
                                    )
                                ) {
                                    append(part.text)
                                }
                            }
                        },
                        modifier = Modifier.padding(
                            bottom = 16.dp,
                            start = 4.dp,
                            end = 4.dp
                        ),
                        style = LocalTextStyle.current.copy(
                            textIndent = TextIndent(firstLine = 20.sp),
                            textAlign = TextAlign.Justify
                        )
                    )
                }

                is DescriptionBlock.Heading3 -> {
                    Text(
                        text = buildAnnotatedString {
                            block.parts.forEach { part ->
                                withStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = if (part.italic) {
                                            FontStyle.Italic
                                        } else {
                                            FontStyle.Normal
                                        }
                                    )
                                ) {
                                    append(part.text)
                                }
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 4.dp,
                            top = 12.dp,
                            bottom = 8.dp
                        )
                    )
                }

                is DescriptionBlock.Heading4 -> {
                    Text(
                        text = buildAnnotatedString {
                            block.parts.forEach { part ->
                                withStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = if (part.italic) {
                                            FontStyle.Italic
                                        } else {
                                            FontStyle.Normal
                                        }
                                    )
                                ) {
                                    append(part.text)
                                }
                            }
                        },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 4.dp,
                            top = 12.dp,
                            bottom = 6.dp
                        )
                    )
                }

                is DescriptionBlock.Heading5 -> {
                    Text(
                        text = buildAnnotatedString {
                            block.parts.forEach { part ->
                                withStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = if (part.italic) {
                                            FontStyle.Italic
                                        } else {
                                            FontStyle.Normal
                                        }
                                    )
                                ) {
                                    append(part.text)
                                }
                            }
                        },
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(
                            start = 8.dp,
                            end = 4.dp,
                            top = 12.dp,
                            bottom = 4.dp
                        )
                    )
                }

                is DescriptionBlock.BulletList -> {
                    Column(
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 4.dp,
                            bottom = 16.dp
                        )
                    ) {
                        block.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "•",
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                Text(
                                    text = buildAnnotatedString {
                                        item.forEach { part ->
                                            withStyle(
                                                SpanStyle(
                                                    fontWeight = if (part.bold) {
                                                        FontWeight.Bold
                                                    } else {
                                                        FontWeight.Normal
                                                    },
                                                    fontStyle = if (part.italic) {
                                                        FontStyle.Italic
                                                    } else {
                                                        FontStyle.Normal
                                                    }
                                                )
                                            ) {
                                                append(part.text)
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}