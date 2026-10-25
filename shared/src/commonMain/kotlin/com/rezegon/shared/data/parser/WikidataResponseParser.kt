
package com.rezegon.shared.data.parser

import com.rezegon.shared.domain.model.WikidataInfo
import com.rezegon.shared.domain.model.WikidataTime

/**
 * Parses a Wikidata API response into the domain model.
 *
 * Keeps the existing extraction rules used by WikidataRepositoryImpl.
 */
class WikidataResponseParser {

    fun parse(
        wikidataId: String,
        response: Map<String, Any>
    ): WikidataInfo? {
        val entities = response["entities"] as? Map<*, *>
            ?: return null

        val entity = entities[wikidataId] as? Map<*, *>
            ?: return null

        val claims = entity["claims"] as? Map<*, *>
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
        claims: Map<*, *>
    ): Long? {
        val statements = claims["P1082"] as? List<*>
            ?: return null

        val candidates = statements.mapNotNull { statement ->
            val statementMap = statement as? Map<*, *>
                ?: return@mapNotNull null

            if (statementMap["rank"] == "deprecated") {
                return@mapNotNull null
            }

            val mainsnak = statementMap["mainsnak"] as? Map<*, *>
                ?: return@mapNotNull null

            val datavalue = mainsnak["datavalue"] as? Map<*, *>
                ?: return@mapNotNull null

            val value = datavalue["value"] as? Map<*, *>
                ?: return@mapNotNull null

            val amount = value["amount"] as? String
                ?: return@mapNotNull null

            val population = amount
                .replace("+", "")
                .toDoubleOrNull()
                ?.toLong()
                ?: return@mapNotNull null

            val qualifiers = statementMap["qualifiers"] as? Map<*, *>
            val pointInTimeStatements =
                qualifiers?.get("P585") as? List<*>

            val year = pointInTimeStatements
                ?.firstOrNull()
                ?.let(::extractYearFromTimeStatement)

            PopulationCandidate(population, year)
        }

        return candidates
            .maxByOrNull { it.year ?: Int.MIN_VALUE }
            ?.population
    }

    private fun extractAreaInSquareKilometers(
        claims: Map<*, *>
    ): Double? {
        val statements = claims["P2046"] as? List<*>
            ?: return null

        val candidates = statements.mapNotNull { statement ->
            val statementMap = statement as? Map<*, *>
                ?: return@mapNotNull null

            if (statementMap["rank"] == "deprecated") {
                return@mapNotNull null
            }

            val mainsnak = statementMap["mainsnak"] as? Map<*, *>
                ?: return@mapNotNull null

            val datavalue = mainsnak["datavalue"] as? Map<*, *>
                ?: return@mapNotNull null

            val value = datavalue["value"] as? Map<*, *>
                ?: return@mapNotNull null

            val amount = value["amount"] as? String
                ?: return@mapNotNull null

            val unit = value["unit"] as? String
                ?: return@mapNotNull null

            if (!unit.contains("Q712226")) {
                return@mapNotNull null
            }

            amount.replace("+", "").toDoubleOrNull()
        }

        return candidates.firstOrNull()
    }

    private fun extractElevationInMeters(
        claims: Map<*, *>
    ): Double? {
        val statements = claims["P2044"] as? List<*>
            ?: return null

        val candidates = statements.mapNotNull { statement ->
            val statementMap = statement as? Map<*, *>
                ?: return@mapNotNull null

            if (statementMap["rank"] == "deprecated") {
                return@mapNotNull null
            }

            val mainsnak = statementMap["mainsnak"] as? Map<*, *>
                ?: return@mapNotNull null

            val datavalue = mainsnak["datavalue"] as? Map<*, *>
                ?: return@mapNotNull null

            val value = datavalue["value"] as? Map<*, *>
                ?: return@mapNotNull null

            val amount = value["amount"] as? String
                ?: return@mapNotNull null

            val unit = value["unit"] as? String
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
        claims: Map<*, *>,
        property: String
    ): WikidataTime? {
        val statements = claims[property] as? List<*>
            ?: return null

        val statement = statements.firstOrNull() as? Map<*, *>
            ?: return null

        val mainsnak = statement["mainsnak"] as? Map<*, *>
            ?: return null

        val datavalue = mainsnak["datavalue"] as? Map<*, *>
            ?: return null

        val value = datavalue["value"] as? Map<*, *>
            ?: return null

        val time = value["time"] as? String
            ?: return null

        val precision = (value["precision"] as? Number)?.toInt()

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
        statement: Any?
    ): Int? {
        val statementMap = statement as? Map<*, *>
            ?: return null

        val mainsnak = statementMap["mainsnak"] as? Map<*, *>
            ?: return null

        val datavalue = mainsnak["datavalue"] as? Map<*, *>
            ?: return null

        val value = datavalue["value"] as? Map<*, *>
            ?: return null

        val time = value["time"] as? String
            ?: return null

        return Regex("""[+-](\d{1,})""")
            .find(time)
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull()
    }

    private data class PopulationCandidate(
        val population: Long,
        val year: Int?
    )
}


