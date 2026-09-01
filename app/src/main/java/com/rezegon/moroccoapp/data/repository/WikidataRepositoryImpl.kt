package com.rezegon.moroccoapp.data.repository

import android.util.Log
import com.rezegon.moroccoapp.data.api.WikidataApi
import com.rezegon.moroccoapp.domain.model.WikidataInfo
import com.rezegon.moroccoapp.domain.model.WikidataTime
import com.rezegon.moroccoapp.domain.repository.WikidataCache
import com.rezegon.moroccoapp.domain.repository.WikidataRepository

class WikidataRepositoryImpl(
    private val api: WikidataApi,
    private val cache: WikidataCache
) : WikidataRepository {

    override suspend fun getInfo(
        wikidataId: String
    ): WikidataInfo? {

        Log.d(
            "WIKIDATA_REPO",
            "START getInfo($wikidataId)"
        )

        cache.get(wikidataId)?.let { cachedInfo ->

            Log.d(
                "WIKIDATA_CACHE",
                "CACHE HIT: $wikidataId"
            )

            return cachedInfo
        }

        Log.d(
            "WIKIDATA_CACHE",
            "CACHE MISS: $wikidataId"
        )

        return try {

            val response = api.getEntity(wikidataId)

            Log.d(
                "WIKIDATA_REPO",
                "API RESPONSE received"
            )

            val entities = response["entities"] as? Map<*, *>
                ?: return null

            val entity = entities[wikidataId] as? Map<*, *>
                ?: return null

            val claims = entity["claims"] as? Map<*, *>
                ?: return null

            val population = extractLatestPopulation(claims)

            val area = extractAreaInSquareKilometers(claims)

            val elevation = extractElevationInMeters(claims)

            val inception = extractTime(
                claims = claims,
                property = "P571"
            )

            val info = WikidataInfo(
                wikidataId = wikidataId,
                population = population,
                area = area,
                elevation = elevation,
                inception = inception
            )

            cache.put(info)

            Log.d(
                "WIKIDATA_INFO",
                "population=$population, area=$area, " +
                        "inceptionYear=${inception?.year}, " +
                        "precision=${inception?.precision}, " +
                        "raw=${inception?.raw}"
            )

            info

        } catch (e: Exception) {

            Log.e(
                "WIKIDATA_ERROR",
                "Błąd podczas pobierania Wikidata",
                e
            )

            null
        }
    }

    /**
     * Pobiera najnowszą nie-deprecated wartość populacji.
     *
     * Jeżeli statement ma qualifier P585 (point in time),
     * używamy tej daty do wyboru najnowszego pomiaru.
     */
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
                ?.let { qualifier ->
                    extractYearFromTimeStatement(qualifier)
                }

            PopulationCandidate(
                population = population,
                year = year
            )
        }

        return candidates
            .maxByOrNull { it.year ?: Int.MIN_VALUE }
            ?.population
    }

    /**
     * Pobiera P2046 tylko wtedy, gdy jednostką jest kilometr kwadratowy.
     *
     * Wikidata przechowuje jednostkę w polu "unit".
     */
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

            amount
                .replace("+", "")
                .toDoubleOrNull()
        }

        return candidates.firstOrNull()
    }

    /**
     * Pobiera wysokość obiektu nad poziomem morza z właściwości P2044 Wikidata.
     *
     * Wikidata może zwrócić wysokość w różnych jednostkach.
     * Funkcja konwertuje obsługiwane jednostki do metrów.
     *
     * Obsługiwane jednostki:
     * - Q11573  → metr
     * - Q828224 → kilometr
     * - Q3710   → stopa
     * - Q93318  → mila morska
     *
     * Zwraca wysokość w metrach lub null, jeśli nie ma poprawnej wartości
     * albo jednostka nie jest obsługiwana.
     */
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
                unit.contains("Q11573") -> {
                    numericValue
                }

                unit.contains("Q828224") -> {
                    numericValue * 1000
                }

                unit.contains("Q3710") -> {
                    numericValue * 0.3048
                }

                unit.contains("Q93318") -> {
                    numericValue * 1852
                }

                else -> {
                    null
                }
            }
        }

        return candidates.firstOrNull()
    }

    private fun extractQuantity(
        claims: Map<*, *>,
        property: String
    ): Double? {

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

        val amount = value["amount"] as? String
            ?: return null

        return amount
            .replace("+", "")
            .toDoubleOrNull()
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

        val datavalue = mainsnak?.get("datavalue") as? Map<*, *>
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

