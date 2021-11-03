package com.dementiaquiz.android.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.User
import timber.log.Timber

class QuizAnswersListAdapter(): ListAdapter<QuizAnswer, QuizAnswersListAdapter.QuizAnswersViewHolder>(
    QuizAnswersListAdapter.QuizAnswersComparator()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAnswersViewHolder {
        return QuizAnswersListAdapter.QuizAnswersViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: QuizAnswersViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


    class QuizAnswersViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {


        //TODO: bind more views with data
        private val questionDescriptionTextView: TextView = itemView.findViewById(R.id.questionDescriptionTextView)
        private val correctnessTextView: TextView = itemView.findViewById(R.id.correctnessTextView)
        private val patientResponseTextView: TextView = itemView.findViewById(R.id.patientResponseTextView)
        private val correctAnswersTextView: TextView = itemView.findViewById(R.id.correctAnswersTextView)

        fun bind(quizAnswer: QuizAnswer) {

            // update the layout based on the quizAnswer retrieved from the database
            questionDescriptionTextView.text = "Question: ${quizAnswer.questionDescription}"

            if(quizAnswer.correct){
                correctnessTextView.text = "✔ The patients's response is correct"
            }else{
                correctnessTextView.text = "✗ The patients's response is wrong"
            }

            if(quizAnswer.response.isBlank()){
                patientResponseTextView.text = "Patient's response: \n"+" ● "+"[Patient did not answer this question]"
            }else {
                patientResponseTextView.text = "Patient's response: \n" +" ● "+ quizAnswer.response
            }

            val correctAnswerList: List<String> = quizAnswer.correctAnswer.split("&")
            var formattedCorrectAnswers:String = "\n"
            for (correctAnswer in correctAnswerList){
                formattedCorrectAnswers+=" ● "+correctAnswer+"\n"
            }
            correctAnswersTextView.text = "Correct answers: "+formattedCorrectAnswers

        }

        companion object {
            fun create(parent: ViewGroup): QuizAnswersViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_answer, parent, false)
                return QuizAnswersViewHolder(view)
            }
        }

    }


    class QuizAnswersComparator : DiffUtil.ItemCallback<QuizAnswer>() {
        override fun areItemsTheSame(
            oldQuizAnswer: QuizAnswer,
            newQuizAnswer: QuizAnswer
        ): Boolean {

            return oldQuizAnswer === newQuizAnswer
        }

        override fun areContentsTheSame(
            oldQuizAnswer: QuizAnswer,
            newQuizAnswer: QuizAnswer
        ): Boolean {
            return (oldQuizAnswer.correct == newQuizAnswer.correct)
                    && (oldQuizAnswer.answerId == newQuizAnswer.answerId)
                    && (oldQuizAnswer.correctAnswer == newQuizAnswer.correctAnswer)
                    && (oldQuizAnswer.questionDescription == newQuizAnswer.questionDescription)
                    && (oldQuizAnswer.response == newQuizAnswer.response)
                    && (oldQuizAnswer.resultId == newQuizAnswer.resultId)

        }
    }

}