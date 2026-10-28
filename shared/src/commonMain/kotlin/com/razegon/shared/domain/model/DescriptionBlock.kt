package com.razegon.shared.domain.model

sealed interface DescriptionBlock {

    data class Paragraph(
        val parts: List<TextPart>
    ) : DescriptionBlock

    data class Heading3(
        val parts: List<TextPart>
    ) : DescriptionBlock

    data class Heading4(
        val parts: List<TextPart>
    ) : DescriptionBlock

    data class Heading5(
        val parts: List<TextPart>
    ) : DescriptionBlock

    data class BulletList(
        val items: List<List<TextPart>>
    ) : DescriptionBlock
}

data class TextPart(
    val text: String,
    val bold: Boolean = false,
    val italic: Boolean = false
)