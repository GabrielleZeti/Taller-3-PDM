package com.pdmcourse2026.basictemplate.data

import android.content.Context
import com.pdmcourse2026.basictemplate.data.database.AppDatabase
import com.pdmcourse2026.basictemplate.data.remote.KtorClient
import com.pdmcourse2026.basictemplate.data.repository.OptionRepository
import com.pdmcourse2026.basictemplate.data.repository.OptionRepositoryImpl
import com.pdmcourse2026.basictemplate.data.repository.PlaceRepository
import com.pdmcourse2026.basictemplate.data.repository.PlaceRepositoryImpl
import com.pdmcourse2026.basictemplate.data.repository.QuestionRepository
import com.pdmcourse2026.basictemplate.data.repository.QuestionRepositoryImpl

class AppProvider(context: Context) {

    private val appDatabase = AppDatabase.getDatabase(context)

    private val questionDao = appDatabase.questionDao()
    private val optionDao = appDatabase.optionDao()

    private val questionRepository: QuestionRepository =
        QuestionRepositoryImpl(questionDao)

    private val optionRepository: OptionRepository =
        OptionRepositoryImpl(optionDao)

    private val placeRepository: PlaceRepository =
        PlaceRepositoryImpl(KtorClient.client)

    fun provideQuestionRepository(): QuestionRepository {
        return questionRepository
    }

    fun provideOptionRepository(): OptionRepository {
        return optionRepository
    }

    fun providePlaceRepository(): PlaceRepository {
        return placeRepository
    }
}