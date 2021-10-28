package com.dementiaquiz.android.models

import androidx.lifecycle.*
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.repositories.QuizResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QuizResultViewModel(private val quizResultRepository: QuizResultRepository):ViewModel(){

    /**
     * Cite from "https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9"
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(quizResult: QuizResult) = viewModelScope.launch {

        quizResultRepository.insert(quizResult)
    }

    // The comment below is quoted from "https://developer.android.com/codelabs/android-room-with-a-view-kotlin#9"
    // Using LiveData and caching have several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    fun getQuizResults():LiveData<List<QuizResult>>{
        return quizResultRepository.getQuizResults().asLiveData()
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