package com.dementiaquiz.android.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.User

class UserListAdapter: ListAdapter<User, UserListAdapter.UserViewHolder>(UsersComparator()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.nickname)
    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO: bind more views with data
        private val userNickNameItemView: TextView = itemView.findViewById(R.id.userNickNametextView)

        fun bind(nickName: String?) {
            userNickNameItemView.text = nickName
        }

        companion object {
            fun create(parent: ViewGroup): UserViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_user, parent, false)
                return UserViewHolder(view)
            }
        }
    }


    class UsersComparator : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {

            return oldUser === newUser
        }

        override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
            return (oldUser.userId == newUser.userId)
                    &&(oldUser.dateOfBirth == newUser.dateOfBirth)
                    &&(oldUser.firstName == newUser.firstName)
                    &&(oldUser.lastName == newUser.lastName)
                    &&(oldUser.gender == newUser.gender)
                    &&(oldUser.nickname == newUser.nickname)
        }
    }


}





