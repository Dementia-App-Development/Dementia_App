package com.dementiaquiz.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dementiaquiz.android.QuizApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * TODO: update description for this class (currently unused)
 */
class QuizViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response

    /**
     * Call getAllQuizQuestions() on init so we can display status immediately.
     */
    init {
        getAllQuizQuestions()
    }

    /**
     * Gets quiz questions via network API
     */
    private fun getAllQuizQuestions() {
        QuizApi.retrofitService.getAllQuizQuestions().enqueue( object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
            }
        })
    }
}