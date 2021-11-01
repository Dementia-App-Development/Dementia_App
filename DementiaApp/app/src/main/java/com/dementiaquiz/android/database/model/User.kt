package com.dementiaquiz.android.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*


/*@Entity(indices = [Index(value = ["first_name", "last_name"],
    unique = true)])*/
@Entity(tableName = "user",indices = [Index(value = ["nickname"],
    unique = true)])
data class User (

    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0L,
    val nickname: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Date,
    //Male: true, Female: false
    val gender: Boolean

)