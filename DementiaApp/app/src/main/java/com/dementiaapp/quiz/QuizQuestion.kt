package com.dementiaapp.quiz

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.IOException

/**
 * A class representing a quiz question
 */
data class QuizQuestion(
    val id: Int,
    val question_no: Int,
    val instruction: String,
    val sub_text: String,
    val time_limit: Int,
    val mark: Int,
    val response_type: ResponseType,
    val answer_verification: AnswerVerification,
    val image_url: String?,
    val audio_url: String?,
    val sub_question: Char?,
    val answers: List<String>?) {

    // Possible methods in which to provide a response for the question
    enum class ResponseType {
        @SerializedName("LIST") LIST,
        @SerializedName("TEXT") TEXT,
        @SerializedName("DATE") DATE,
        @SerializedName("SPEECH") SPEECH,
        @SerializedName("CAMERA") CAMERA,
        @SerializedName("ASSISTED") ASSISTED}

    // Possible methods in which a question's response can be verified against the correct answer
    enum class AnswerVerification {
        @SerializedName("LIST") LIST,
        @SerializedName("GPS") GPS,
        @SerializedName("ASSISTED") ASSISTED}
}


/**
 * Parses the json file from the assets folder and returns the contents of the file as a string
 */
fun getJsonDataFromAsset(context: Context, fileName: String): String {
    var jsonString: String = ""
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
    }
    return jsonString
}


/**
 * Parses a json string and outputs a list of QuizQuestion objects
 */
fun generateQuizQuestions(jsonString: String): List<QuizQuestion> {
    try {
        val gson = Gson()
        val quizQuestion = object : TypeToken<List<QuizQuestion>>() {}.type
        return gson.fromJson(jsonString, quizQuestion)
    } catch (e: Exception) {
        Log.i("error:", "could not generate quiz questions list from json string")
        throw Exception("could not generate quiz questions list from json string")
    }
}