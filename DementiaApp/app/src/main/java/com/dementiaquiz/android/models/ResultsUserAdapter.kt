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
import com.dementiaquiz.android.databinding.FragmentResultsBinding
import com.dementiaquiz.android.databinding.RecyclerviewItemBinding

class ResultsUserAdapter : ListAdapter<User,
        ResultsUserAdapter.NicknameViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: NicknameViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NicknameViewHolder {
        return NicknameViewHolder.from(parent)
    }

    class NicknameViewHolder private constructor(val binding: RecyclerviewItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {
            binding.user = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NicknameViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemBinding.inflate(layoutInflater, parent, false)
                return NicknameViewHolder(binding)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.nickname == newItem.nickname
    }
}


