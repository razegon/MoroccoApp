package com.rezegon.shared.data.quiz

import com.rezegon.shared.data.source.JsonResourceReader
import com.rezegon.shared.domain.repository.QuizQuestionDataSourceInterface
import kotlinx.serialization.json.Json

class JsonQuizQuestionDataSource(
    private val resourceReader: JsonResourceReader
) : QuizQuestionDataSourceInterface {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override suspend fun loadQuestions(): List<QuizQuestionDto> {
        val text = resourceReader.readText("files/quiz/questions.json")
        return json.decodeFromString<List<QuizQuestionDto>>(text)
    }
}