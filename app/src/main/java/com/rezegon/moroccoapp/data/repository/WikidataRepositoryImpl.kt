package com.rezegon.moroccoapp.data.repository

import android.util.Log
import com.rezegon.moroccoapp.data.api.WikidataApi
import com.rezegon.moroccoapp.data.cache.WikidataDao
import com.rezegon.moroccoapp.data.cache.toDomain
import com.rezegon.moroccoapp.data.cache.toEntity
import com.rezegon.moroccoapp.domain.model.WikidataInfo
import com.rezegon.moroccoapp.domain.model.WikidataTime
import com.rezegon.moroccoapp.domain.repository.WikidataCache
import com.rezegon.moroccoapp.domain.repository.WikidataRepository

class WikidataRepositoryImpl(
    private val api: WikidataApi,
    private val cache: WikidataCache,
    private val dao: WikidataDao
) : WikidataRepository {

    /**
     * Returns Wikidata information for the given entity.
     *
     * Data is loaded in the following order:
     * 1. In-memory cache
     * 2. Persistent Room database
     * 3. Wikidata API
     *
     * Data received from the API is stored in both Room and memory cache.
     */
    override suspend fun getInfo(
        wikidataId: String
    ): WikidataInfo? {

        Log.d(
            "WIKIDATA_REPO",
            "START getInfo($wikidataId)"
        )

        // First level cache: fast in-memory cache.
        cache.get(wikidataId)?.let { cachedInfo ->

            Log.d(
                "WIKIDATA_CACHE",
                "MEMORY CACHE HIT: $wikidataId"
            )

            return cachedInfo
        }

        Log.d(
            "WIKIDATA_CACHE",
            "MEMORY CACHE MISS: $wikidataId"
        )

        // Second level cache: persistent Room database.
        try {
            dao.get(wikidataId)?.let { entity ->

                val info = entity.toDomain()

                Log.d(
                    "WIKIDATA_ROOM",
                    "ROOM CACHE HIT: $wikidataId"
                )

                // Put the data back into memory cache
                // so future requests are faster.
                cache.put(info)

                return info
            }

            Log.d(
                "WIKIDATA_ROOM",
                "ROOM CACHE MISS: $wikidataId"
            )

        } catch (e: Exception) {

            // A Room failure should not prevent us from trying
            // the API when an internet connection is available.
            Log.e(
                "WIKIDATA_ROOM",
                "Błąd odczytu z Room dla $wikidataId",
                e
            )
        }

        // Last level: fetch fresh data from the Wikidata API.
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

            // Persistent cache.
            try {
                dao.upsert(info.toEntity())

                Log.d(
                    "WIKIDATA_ROOM",
                    "ROOM CACHE UPDATE: $wikidataId"
                )

            } catch (e: Exception) {

                // The API result is still valid even if
                // storing it locally fails.
                Log.e(
                    "WIKIDATA_ROOM",
                    "Błąd zapisu do Room dla $wikidataId",
                    e
                )
            }

            // Fast in-memory cache.
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
     * Extracts the newest non-deprecated population value.
     *
     * When the statement contains the P585 (point in time)
     * qualifier, its year is used to determine the newest value.
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
     * Extracts P2046 only when the value is expressed in square kilometres.
     *
     * Wikidata stores the unit identifier in the "unit" field.
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
     * Extracts elevation from Wikidata property P2044
     * and converts supported units to metres.
     *
     * Supported units:
     * Q11573  → metre
     * Q828224 → kilometre
     * Q3710   → foot
     * Q93318  → nautical mile
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

    /**
     * Extracts a time value from the given Wikidata property.
     *
     * The raw Wikidata time string, year and precision are preserved.
     */
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

    /**
     * Extracts the year from a Wikidata time statement.
     *
     * This is used for P585 qualifiers when selecting
     * the newest population measurement.
     */
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