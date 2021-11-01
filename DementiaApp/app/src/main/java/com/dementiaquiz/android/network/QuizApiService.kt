package com.dementiaquiz.android

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
    // Gets all solo questions, dummy data with fixed latitude and longitude
    @GET("question/all/?lat=51.487580&long=-0.190920&mode=solo")
    fun getAllTestQuestions():
            Call<String>

    @GET("question/all/")
    fun getAllCustomQuestions(
        @Query("lat") lat: String,
        @Query("long") long: String,
        @Query("mode") mode: String
    ): Call<String>

    @GET("question/all/")
    fun getAllCustomQuestionsNoGPS(
        @Query("mode") mode: String
    ): Call<String>
}

/**
 * API object to be used for retrieving quiz questions
 */
object QuizApi {
    val retrofitService : QuizApiService by lazy {
        retrofit.create(QuizApiService::class.java)
    }
}