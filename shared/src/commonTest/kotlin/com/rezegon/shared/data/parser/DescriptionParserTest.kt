package com.rezegon.shared.data.parser

import com.rezegon.shared.domain.model.DescriptionBlock
import com.rezegon.shared.domain.model.TextPart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DescriptionParserTest {

    @Test
    fun parsesHeadingAndParagraphWithFormatting() {
        val html = """
            <h4>Historia</h4>
            <p>Miasto zostało założone przez <b>Almorawidów</b> i było <i>ważnym</i> ośrodkiem.</p>
        """.trimIndent()

        val result = DescriptionParser.parse(html)

        assertEquals(2, result.size)
        assertTrue(result[0] is DescriptionBlock.Heading4)
        assertTrue(result[1] is DescriptionBlock.Paragraph)

        val paragraph = result[1] as DescriptionBlock.Paragraph

        assertEquals(
            "Miasto zostało założone przez Almorawidów i było ważnym ośrodkiem.",
            paragraph.parts.joinToString("") { it.text }
        )

        assertEquals(true, paragraph.parts[1].bold)
        assertEquals(false, paragraph.parts[1].italic)

        assertEquals(false, paragraph.parts[3].bold)
        assertEquals(true, paragraph.parts[3].italic)

    }

    @Test
    fun parsesBulletList() {
        val html = """
        <ul>
            <li>Pierwszy punkt</li>
            <li>Drugi punkt</li>
        </ul>
    """.trimIndent()

        val result = DescriptionParser.parse(html)

        assertEquals(1, result.size)
        assertTrue(result[0] is DescriptionBlock.BulletList)

        val bulletList = result[0] as DescriptionBlock.BulletList

        assertEquals(
            listOf(
                listOf(TextPart("Pierwszy punkt")),
                listOf(TextPart("Drugi punkt"))
            ),
            bulletList.items
        )
    }

    @Test
    fun parsesFormattingInsideBulletList() {
        val html = """
        <ul>
            <li>Pierwszy <b>ważny</b> punkt</li>
            <li>Drugi <i>ciekawy</i> punkt</li>
        </ul>
    """.trimIndent()

        val result = DescriptionParser.parse(html)

        assertEquals(1, result.size)
        assertTrue(result[0] is DescriptionBlock.BulletList)

        val bulletList = result[0] as DescriptionBlock.BulletList

        println(bulletList.items)

        assertEquals(
            listOf(
                listOf(
                    TextPart("Pierwszy "),
                    TextPart("ważny", bold = true),
                    TextPart(" punkt")
                ),
                listOf(
                    TextPart("Drugi "),
                    TextPart("ciekawy", italic = true),
                    TextPart(" punkt")
                )
            ),
            bulletList.items
        )
    }
}

