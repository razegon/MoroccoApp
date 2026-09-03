package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.QuizQuestion

interface QuizRepository {

    fun getQuestions(): List<QuizQuestion>
}