package com.dementiaapp.quiz.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dementiaapp.quiz.models.QuizViewModel
import com.dementiaapp.quiz.R
import com.dementiaapp.quiz.databinding.FragmentQuizBinding
import com.dementiaapp.quiz.models.QuizQuestion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Displays the quiz questions and corresponding UI elements for answering them
 */
class QuizFragment : Fragment() {

    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0

    // Initialize the quiz questions list
    private lateinit var quizQuestions : List<QuizQuestion>

    /**
     * Lazily initialize our [QuizViewModel].
     * //TODO: need to implement ViewModel
     */
    private val viewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }


    /**
     * Fetch quiz questions for the quiz
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Below code generates placeholder two-member list, need to replace so that it fetches data from server
        val questionOne = QuizQuestion(
            1,
            1,
            "What year is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.LIST,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'a',
            listOf("2021"),
            listOf("2021")
        )
        val questionTwo = QuizQuestion(
            2,
            1,
            "What season is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.LIST,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'b',
            listOf("Spring"),
            listOf("Spring")
        )
        var quizQuestions = listOf(questionOne, questionTwo)
        // TODO: end of placeholder code


        //TODO implement quiz questions JSON from API and uncomment when done
        //TODO should be in onStart method
//        var quizQuestions = generateQuizQuestionsFromJson(quizQuestionsJsonString)

        // Sort the questions list by id
        quizQuestions = quizQuestions.sortedBy { it.id }
    }


    /**
     * Create views for the quiz by inflating XML
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater,
            R.layout.fragment_quiz, container, false)

        // Initialize the question fields in the UI
        populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)

        // When next button is pressed, go to next question so long as not at end of question list
        binding.btnNext.setOnClickListener {
            if (currentQuestionIndex < quizQuestions.size - 1) {
                currentQuestionIndex += 1
                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
            }
        }

        // When prev button is pressed, go to prev question so long as not at start of question list
        binding.btnPrevious.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex -= 1
                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
            }
        }

        return binding.root
    }
}




/**
 * Parses a json string and outputs a list of QuizQuestion objects
 */
fun generateQuizQuestionsFromJson(jsonString: String): List<QuizQuestion> {
    try {
        val gson = Gson()
        val quizQuestion = object : TypeToken<List<QuizQuestion>>() {}.type
        return gson.fromJson(jsonString, quizQuestion)
    } catch (e: Exception) {
        Log.i("error:", "could not generate quiz questions list from json string") //TODO: replace with Timber
        throw Exception("could not generate quiz questions list from json string")
    }
}


/**
 * This function populates the UI with the question at the provided index in the list of questions
 */
// TODO: temporary code for demonstration purposes only - needs tidy up when UI is done to display components properly
fun populateUIWithQuestion(
    binding: FragmentQuizBinding,
    quizQuestions: List<QuizQuestion>,
    i: Int
) {
    // Instantiate the fields in the main activity with the first question in the quiz
    binding.tvTimeLimit.text = quizQuestions[i].time_limit.toString() + " seconds to answer"
    binding.tvQuestionNo.text = "id: " + quizQuestions[i].id.toString() + " | " +
            "Question " + quizQuestions[i].question_no.toString() +
            quizQuestions[i].sub_question.toString()
    binding.tvInstructions.text = quizQuestions[i].instruction
    binding.tvSubText.text = quizQuestions[i].sub_text
}