package com.dementiaquiz.android.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://alz-backend.herokuapp.com/"

/**
 * Add an instance of ScalarsConverterFactory and the BASE_URL we provided, then call build() to create the Retrofit object
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * Quiz API endpoint for retrieving quiz questions from the server
 */
interface QuizApiService {
    @GET("question/all/?lat=51.487580&long=-0.190920&mode=solo") //TODO this parameter retrieves all question, need to be location & mode specific
    fun getAllQuestions():
            Call<String>
}

/**
 * API object to be used for retrieving quiz questions
 */
object QuizApi {
    val retrofitService: QuizApiService by lazy {
        retrofit.create(QuizApiService::class.java)
    }
}