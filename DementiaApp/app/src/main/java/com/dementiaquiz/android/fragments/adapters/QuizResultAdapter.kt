package com.dementiaquiz.android.fragments.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.QuizResult
import timber.log.Timber
import java.text.SimpleDateFormat

/**
 * Used in Quiz Result Recycler View
 */
class QuizResultAdapter(private val onItemClicked: (resultId: Long) -> Unit) :
    ListAdapter<QuizResult, QuizResultAdapter.QuizResultViewHolder>(
        QuizResultComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizResultViewHolder {
        return QuizResultViewHolder.create(parent, onItemClicked)
    }

    override fun onBindViewHolder(holder: QuizResultViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


    class QuizResultViewHolder(
        itemView: View,
        private val onItemClicked: (resultId: Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        // initialize the onclick listener
        init {
            itemView.setOnClickListener(this)
        }

        private val scoreTextView: TextView = itemView.findViewById(R.id.ScoreTextView)
        private val testTimeTextView: TextView = itemView.findViewById(R.id.TestTimeTextView)

        private lateinit var currentQuizResult: QuizResult

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(quizResult: QuizResult) {
            //save an instance of list item in memory for each row (quizResult)
            currentQuizResult = quizResult

            val dateFormated = SimpleDateFormat("yyyy-MM-dd HH:mm").format(quizResult.timeCreated)
            Timber.i("the formatted date is: $dateFormated")

            // update the layout
            scoreTextView.text = "Score: ${currentQuizResult.score}%"
            testTimeTextView.text = "Test Time: $dateFormated"
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onItemClicked: (resultId: Long) -> Unit
            ): QuizResultViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_result, parent, false)
                return QuizResultViewHolder(view, onItemClicked)
            }
        }

        // implement the onclick listener
        override fun onClick(p0: View?) {

            adapterPosition

            Timber.i("Onclick called -> $currentQuizResult")

            onItemClicked(currentQuizResult.resultId)

        }
    }

    class QuizResultComparator : DiffUtil.ItemCallback<QuizResult>() {

        override fun areItemsTheSame(
            oldQuizResult: QuizResult,
            newQuizResult: QuizResult
        ): Boolean {

            return oldQuizResult === newQuizResult
        }

        override fun areContentsTheSame(
            oldQuizResult: QuizResult,
            newQuizResult: QuizResult
        ): Boolean {
            return (oldQuizResult.resultId == newQuizResult.resultId)
                    && (oldQuizResult.score == newQuizResult.score)
                    && (oldQuizResult.timeCreated == newQuizResult.timeCreated)
                    && (oldQuizResult.userId == newQuizResult.userId)

        }
    }


}