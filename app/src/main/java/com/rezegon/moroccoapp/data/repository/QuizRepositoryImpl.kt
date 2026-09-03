package com.rezegon.moroccoapp.data.repository

import com.rezegon.moroccoapp.domain.model.QuizQuestion
import com.rezegon.moroccoapp.domain.repository.QuizRepository

class QuizRepositoryImpl : QuizRepository {

    override fun getQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                question = "Jaka jest stolica Maroka?",
                answers = listOf(
                    "Casablanca",
                    "Rabat",
                    "Marrakesz",
                    "Fez"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "Jaki ocean leży na zachód od Maroka?",
                answers = listOf(
                    "Ocean Indyjski",
                    "Ocean Arktyczny",
                    "Ocean Atlantycki",
                    "Ocean Spokojny"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "Jak nazywa się słynna pustynia obejmująca południową część Maroka?",
                answers = listOf(
                    "Gobi",
                    "Sahara",
                    "Atakama",
                    "Kalahari"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "Które miasto słynie z medyny i placu Dżamaa al-Fina?",
                answers = listOf(
                    "Marrakesz",
                    "Agadir",
                    "Rabat",
                    "Tetuan"
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                question = "Jaki język jest jednym z języków urzędowych Maroka?",
                answers = listOf(
                    "Arabski",
                    "Hiszpański",
                    "Portugalski",
                    "Włoski"
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                question = "Jak nazywa się najwyższy szczyt Maroka?",
                answers = listOf(
                    "Jbel Toubkal",
                    "Jebel Musa",
                    "M'Goun",
                    "Jbel Saghro"
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                question = "Jakie morze leży na północ od Maroka?",
                answers = listOf(
                    "Morze Czarne",
                    "Morze Śródziemne",
                    "Morze Czerwone",
                    "Morze Kaspijskie"
                ),
                correctAnswerIndex = 1
            ),
            QuizQuestion(
                question = "Które miasto jest największym miastem Maroka?",
                answers = listOf(
                    "Rabat",
                    "Fez",
                    "Casablanca",
                    "Marrakesz"
                ),
                correctAnswerIndex = 2
            ),
            QuizQuestion(
                question = "Ait Ben Haddou jest przykładem:",
                answers = listOf(
                    "Kasby / ksaru",
                    "Meczetu",
                    "Pałacu królewskiego",
                    "Portu"
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                question = "Jak nazywa się charakterystyczna marokańska herbata?",
                answers = listOf(
                    "Herbata miętowa",
                    "Herbata masala",
                    "Herbata matcha",
                    "Herbata yerba"
                ),
                correctAnswerIndex = 0
            )
        )
    }
}

