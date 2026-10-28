package com.razegon.shared.data.parser

import com.razegon.shared.domain.model.DescriptionBlock
import com.razegon.shared.domain.model.TextPart

object DescriptionParser {

    fun parse(html: String): List<DescriptionBlock> {
        val blocks = mutableListOf<DescriptionBlock>()

        val blockRegex = Regex(
            """(?s)<(h3|h4|h5|p|ul)>(.*?)</\1>""",
            RegexOption.IGNORE_CASE
        )

        blockRegex.findAll(html).forEach { match ->
            val type = match.groupValues[1].lowercase()
            val content = match.groupValues[2]

            val parts = parseTextParts(content)

            when (type) {
                "h3" -> blocks += DescriptionBlock.Heading3(parts)
                "h4" -> blocks += DescriptionBlock.Heading4(parts)
                "h5" -> blocks += DescriptionBlock.Heading5(parts)
                "p" -> blocks += DescriptionBlock.Paragraph(parts)
                "ul" -> {
                    val items = Regex(
                        """<li>(.*?)</li>""",
                        RegexOption.IGNORE_CASE
                    )
                        .findAll(content)
                        .map { match ->
                            parseTextParts(match.groupValues[1])
                        }
                        .toList()

                    blocks += DescriptionBlock.BulletList(items)
                }
            }
        }

        return blocks
    }

    private fun parseTextParts(text: String): List<TextPart> {
        val parts = mutableListOf<TextPart>()

        val regex = Regex(
            """<(b|strong|i|em)>(.*?)</\1>""",
            RegexOption.IGNORE_CASE
        )

        var lastIndex = 0

        regex.findAll(text).forEach { match ->
            if (match.range.first > lastIndex) {
                parts += TextPart(
                    text = cleanText(text.substring(lastIndex, match.range.first))
                )
            }

            val tag = match.groupValues[1].lowercase()
            val content = cleanText(match.groupValues[2])

            parts += when (tag) {
                "b", "strong" -> TextPart(
                    text = content,
                    bold = true
                )

                "i", "em" -> TextPart(
                    text = content,
                    italic = true
                )

                else -> TextPart(content)
            }

            lastIndex = match.range.last + 1
        }

        if (lastIndex < text.length) {
            parts += TextPart(
                text = cleanText(text.substring(lastIndex))
            )
        }

        return parts
            .mapIndexed { index, part ->
                when {
                    index == 0 -> part.copy(text = part.text.trimStart())
                    index == parts.lastIndex -> part.copy(text = part.text.trimEnd())
                    else -> part
                }
            }
            .filter { it.text.isNotEmpty() }
    }

    private fun cleanText(text: String): String {
        return text
            .replace("\\t", " ")
            .replace(Regex("""\s+"""), " ")
    }
}

