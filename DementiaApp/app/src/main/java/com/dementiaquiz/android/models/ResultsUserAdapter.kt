package com.dementiaquiz.android.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.User

class ResultsUserAdapter: ListAdapter<User, ResultsUserAdapter.NicknameViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NicknameViewHolder {
        return NicknameViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NicknameViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item.nickname)
    }

    class NicknameViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val userNicknameView: TextView = itemView.findViewById(R.id.result_user_list)

        fun bind(text: String?) {
            userNicknameView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): NicknameViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_results, parent, false)
                return NicknameViewHolder(view)
            }
        }
    }

    class UserComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.nickname == newItem.nickname
        }
    }

}


