package com.dementiaquiz.android.models

import androidx.lifecycle.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.repositories.QuizResultRepository
import kotlinx.coroutines.launch

class QuizResultViewModel(private val quizResultRepository: QuizResultRepository):ViewModel(){

    /**
     * Cite from "https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9"
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    // insertQuizResult and answers belong to that result. It returns the ID of
    // the inserted QuizResult
    fun insertQuizResultAndAnswers(quizResult: QuizResult, quizAnswerList:List<QuizAnswer>):Long{

        var quizResultId:Long =-1
        viewModelScope.launch {
            quizResultId = quizResultRepository.insertQuizResultAndAnswers(quizResult,quizAnswerList)
        }
        return quizResultId
    }

    // The comment below is quoted from "https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9"
    // Using LiveData and caching have several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    fun getQuizResults():LiveData<List<QuizResult>>{
        return quizResultRepository.getQuizResults().asLiveData()
    }

    fun getQuizResultsByUserId(userId: Long):LiveData<List<QuizResult>>{
        return quizResultRepository.getQuizResultsByUserId(userId).asLiveData()
    }

    fun getQuizResultByResultId(resultId:Long):LiveData<QuizResult>{
        return quizResultRepository.getQuizResultByResultId(resultId).asLiveData()
    }

}

class QuizResultViewModelFactory(private val quizResultRepository: QuizResultRepository):ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(QuizResultViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return QuizResultViewModel(quizResultRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}