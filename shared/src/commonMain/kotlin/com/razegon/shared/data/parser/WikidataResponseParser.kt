
package com.razegon.shared.data.parser

import com.razegon.shared.domain.model.WikidataInfo
import com.razegon.shared.domain.model.WikidataTime
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Parses a Wikidata API response into the domain model.
 *
 * Uses kotlinx.serialization JsonObject instead of Map<String, Any>,
 * allowing the shared module to process JSON independently of Gson.
 */
class WikidataResponseParser {

    fun parse(
        wikidataId: String,
        response: JsonObject
    ): WikidataInfo? {

        val entity = response["entities"]
            ?.jsonObjectOrNull()
            ?.get(wikidataId)
            ?.jsonObjectOrNull()
            ?: return null

        val claims = entity["claims"]
            ?.jsonObjectOrNull()
            ?: return null

        return WikidataInfo(
            wikidataId = wikidataId,
            population = extractLatestPopulation(claims),
            area = extractAreaInSquareKilometers(claims),
            elevation = extractElevationInMeters(claims),
            inception = extractTime(claims, "P571")
        )
    }

    private fun extractLatestPopulation(
        claims: JsonObject
    ): Long? {

        val statements = claims["P1082"]
            ?.jsonArrayOrNull()
            ?: return null

        val candidates = statements.mapNotNull { statement ->

            val statementObject = statement.jsonObjectOrNull()
                ?: return@mapNotNull null

            if (statementObject.rank() == "deprecated") {
                return@mapNotNull null
            }

            val amount = statementObject.amount()
                ?: return@mapNotNull null

            val population = amount
                .replace("+", "")
                .toDoubleOrNull()
                ?.toLong()
                ?: return@mapNotNull null

            val year = statementObject["qualifiers"]
                ?.jsonObjectOrNull()
                ?.get("P585")
                ?.jsonArrayOrNull()
                ?.firstOrNull()
                ?.let(::extractYearFromTimeStatement)

            PopulationCandidate(population, year)
        }

        return candidates
            .maxByOrNull { it.year ?: Int.MIN_VALUE }
            ?.population
    }

    private fun extractAreaInSquareKilometers(
        claims: JsonObject
    ): Double? {

        val statements = claims["P2046"]
            ?.jsonArrayOrNull()
            ?: return null

        val candidates = statements.mapNotNull { statement ->

            val statementObject = statement.jsonObjectOrNull()
                ?: return@mapNotNull null

            if (statementObject.rank() == "deprecated") {
                return@mapNotNull null
            }

            val unit = statementObject.unit()
                ?: return@mapNotNull null

            if (!unit.contains("Q712226")) {
                return@mapNotNull null
            }

            statementObject.amount()
                ?.replace("+", "")
                ?.toDoubleOrNull()
        }

        return candidates.firstOrNull()
    }

    private fun extractElevationInMeters(
        claims: JsonObject
    ): Double? {

        val statements = claims["P2044"]
            ?.jsonArrayOrNull()
            ?: return null

        val candidates = statements.mapNotNull { statement ->

            val statementObject = statement.jsonObjectOrNull()
                ?: return@mapNotNull null

            if (statementObject.rank() == "deprecated") {
                return@mapNotNull null
            }

            val unit = statementObject.unit()
                ?: return@mapNotNull null

            val amount = statementObject.amount()
                ?: return@mapNotNull null

            val numericValue = amount
                .replace("+", "")
                .toDoubleOrNull()
                ?: return@mapNotNull null

            when {
                unit.contains("Q11573") -> numericValue
                unit.contains("Q828224") -> numericValue * 1000
                unit.contains("Q3710") -> numericValue * 0.3048
                unit.contains("Q93318") -> numericValue * 1852
                else -> null
            }
        }

        return candidates.firstOrNull()
    }

    private fun extractTime(
        claims: JsonObject,
        property: String
    ): WikidataTime? {

        val statements = claims[property]
            ?.jsonArrayOrNull()
            ?: return null

        val statement = statements.firstOrNull()
            ?.jsonObjectOrNull()
            ?: return null

        val value = statement.valueObject()
            ?: return null

        val time = value["time"]
            ?.primitiveContent()
            ?: return null

        val precision = value["precision"]
            ?.primitiveContent()
            ?.toIntOrNull()

        val year = Regex("""^[+-](\d{1,})""")
            .find(time)
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull()

        return WikidataTime(
            year = year,
            precision = precision,
            raw = time
        )
    }

    private fun extractYearFromTimeStatement(
        statement: kotlinx.serialization.json.JsonElement
    ): Int? {

        val value = statement
            .jsonObjectOrNull()
            ?.valueObject()
            ?: return null

        val time = value["time"]
            ?.primitiveContent()
            ?: return null

        return Regex("""[+-](\d{1,})""")
            .find(time)
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull()
    }

    private fun JsonObject.rank(): String? =
        this["rank"]?.primitiveContent()

    private fun JsonObject.amount(): String? =
        this["mainsnak"]
            ?.jsonObjectOrNull()
            ?.get("datavalue")
            ?.jsonObjectOrNull()
            ?.get("value")
            ?.jsonObjectOrNull()
            ?.get("amount")
            ?.primitiveContent()

    private fun JsonObject.unit(): String? =
        this["mainsnak"]
            ?.jsonObjectOrNull()
            ?.get("datavalue")
            ?.jsonObjectOrNull()
            ?.get("value")
            ?.jsonObjectOrNull()
            ?.get("unit")
            ?.primitiveContent()

    private fun JsonObject.valueObject(): JsonObject? =
        this["mainsnak"]
            ?.jsonObjectOrNull()
            ?.get("datavalue")
            ?.jsonObjectOrNull()
            ?.get("value")
            ?.jsonObjectOrNull()

    private fun kotlinx.serialization.json.JsonElement.jsonObjectOrNull(): JsonObject? =
        runCatching { jsonObject }.getOrNull()

    private fun kotlinx.serialization.json.JsonElement.jsonArrayOrNull(): JsonArray? =
        runCatching { jsonArray }.getOrNull()

    private fun kotlinx.serialization.json.JsonElement.primitiveContent(): String? =
        (this as? JsonPrimitive)?.contentOrNull

    private data class PopulationCandidate(
        val population: Long,
        val year: Int?
    )
}
