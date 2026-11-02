
package com.razegon.shared.data.parser

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class WikidataResponseParserTest {

    private val parser = WikidataResponseParser()

    @Test
    fun parsesPopulationAreaElevationAndInception() {
        val response = Json.parseToJsonElement(
            """
            {
              "entities": {
                "Q123": {
                  "claims": {
                    "P1082": [
                      {
                        "rank": "normal",
                        "mainsnak": {
                          "datavalue": {
                            "value": { "amount": "+1000" }
                          }
                        },
                        "qualifiers": {
                          "P585": [
                            {
                              "mainsnak": {
                                "datavalue": {
                                  "value": {
                                    "time": "+2010-00-00T00:00:00Z"
                                  }
                                }
                              }
                            }
                          ]
                        }
                      },
                      {
                        "rank": "normal",
                        "mainsnak": {
                          "datavalue": {
                            "value": { "amount": "+1500" }
                          }
                        },
                        "qualifiers": {
                          "P585": [
                            {
                              "mainsnak": {
                                "datavalue": {
                                  "value": {
                                    "time": "+2020-00-00T00:00:00Z"
                                  }
                                }
                              }
                            }
                          ]
                        }
                      }
                    ],
                    "P2046": [
                      {
                        "rank": "normal",
                        "mainsnak": {
                          "datavalue": {
                            "value": {
                              "amount": "+12.5",
                              "unit": "http://www.wikidata.org/entity/Q712226"
                            }
                          }
                        }
                      }
                    ],
                    "P2044": [
                      {
                        "rank": "normal",
                        "mainsnak": {
                          "datavalue": {
                            "value": {
                              "amount": "+850",
                              "unit": "http://www.wikidata.org/entity/Q11573"
                            }
                          }
                        }
                      }
                    ],
                    "P571": [
                      {
                        "mainsnak": {
                          "datavalue": {
                            "value": {
                              "time": "+0791-00-00T00:00:00Z",
                              "precision": 9
                            }
                          }
                        }
                      }
                    ]
                  }
                }
              }
            }
            """.trimIndent()
        ).jsonObject

        val result = parser.parse("Q123", response)

        assertNotNull(result)
        assertEquals("Q123", result.wikidataId)
        assertEquals(1500L, result.population)
        assertEquals(12.5, result.area)
        assertEquals(850.0, result.elevation)

        assertNotNull(result.inception)
        assertEquals(791, result.inception.year)
        assertEquals(9, result.inception.precision)
        assertEquals("+0791-00-00T00:00:00Z", result.inception.raw)
    }

    @Test
    fun ignoresDeprecatedPopulationStatements() {
        val response = Json.parseToJsonElement(
            """
            {
              "entities": {
                "Q123": {
                  "claims": {
                    "P1082": [
                      {
                        "rank": "deprecated",
                        "mainsnak": {
                          "datavalue": {
                            "value": { "amount": "+99999" }
                          }
                        }
                      },
                      {
                        "rank": "normal",
                        "mainsnak": {
                          "datavalue": {
                            "value": { "amount": "+1500" }
                          }
                        }
                      }
                    ]
                  }
                }
              }
            }
            """.trimIndent()
        ).jsonObject

        val result = parser.parse("Q123", response)

        assertNotNull(result)
        assertEquals(1500L, result.population)
    }

    @Test
    fun returnsNullWhenEntityDoesNotExist() {
        val response = Json.parseToJsonElement(
            """
            {
              "entities": {}
            }
            """.trimIndent()
        ).jsonObject

        assertNull(parser.parse("Q123", response))
    }

    @Test
    fun returnsNullWhenClaimsAreMissing() {
        val response = Json.parseToJsonElement(
            """
            {
              "entities": {
                "Q123": {}
              }
            }
            """.trimIndent()
        ).jsonObject

        assertNull(parser.parse("Q123", response))
    }
}
