package com.dementiaquiz.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.repositories.QuizAnswerRepository
import com.dementiaquiz.android.repositories.QuizResultRepository

class ResultAnswersViewModel(private val quizAnswerRepository: QuizAnswerRepository): ViewModel() {

    // get the quiz answers belong to the given result ID
    fun getQuizAnswersByResultId(resultId:Long): LiveData<List<QuizAnswer>> {
        return quizAnswerRepository.getQuizAnswersByResultId(resultId).asLiveData()
    }


}

class ResultAnswersViewModelFactory(private val quizAnswerRepository: QuizAnswerRepository):
    ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ResultAnswersViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ResultAnswersViewModel(quizAnswerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}