package com.rezegon.moroccoapp.data.quiz

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rezegon.shared.data.quiz.QuizQuestionDto
import com.rezegon.shared.domain.repository.QuizQuestionDataSourceInterface

// Reads quiz question data from the bundled JSON asset.
class QuizQuestionDataSource(
    private val context: Context
) : QuizQuestionDataSourceInterface {

    // Loads the JSON file and converts it into DTO objects.
    override fun loadQuestions(): List<QuizQuestionDto> {
        val json = context.assets
            .open("quiz/questions.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<QuizQuestionDto>>() {}.type

        return Gson().fromJson(json, type)
    }
}