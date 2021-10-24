package com.dementiaquiz.android.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User (

    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0L,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: Date

)