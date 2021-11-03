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
import timber.log.Timber

class UserListAdapter(private val onItemClicked: (userId: Long) -> Unit): ListAdapter<User, UserListAdapter.UserViewHolder>(UsersComparator()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        Timber.i("onCreateViewHolder called")
        return UserViewHolder.create(parent,onItemClicked)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Timber.i("onBindViewHolder called")
        val current = getItem(position)
        holder.bind(current)
    }


    class UserViewHolder(
        itemView: View,
        private val onItemClicked: (userId: Long) -> Unit
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        // initialize the onclick listener
        init {
            itemView.setOnClickListener(this)
        }

        //TODO: bind more views with data
        private val userNickNameItemView: TextView = itemView.findViewById(R.id.userNickNametextView)

        private lateinit var currentUser:User

        fun bind(user: User) {
            currentUser = user
            userNickNameItemView.text = user.nickname
        }

        companion object {
            fun create(parent: ViewGroup, onItemClicked: (userId: Long) -> Unit): UserViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_user, parent, false)
                return UserViewHolder(view, onItemClicked)
            }
        }

        // implement the onclick listener
        override fun onClick(p0: View?) {

            val position = adapterPosition

            Timber.i("userNickNameItemView in onClick is: ${userNickNameItemView.text}")
            Timber.i("the current user is: ${currentUser}")


            onItemClicked(currentUser.userId)

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





