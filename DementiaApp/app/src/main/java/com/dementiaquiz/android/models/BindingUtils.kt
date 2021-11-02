package com.dementiaquiz.android.models

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.dementiaquiz.android.database.model.User

@BindingAdapter("userNickname")
fun TextView.setUserNickname(item: User?) {
    item?.let {
        text = item.nickname
    }
}