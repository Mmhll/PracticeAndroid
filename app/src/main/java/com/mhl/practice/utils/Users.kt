package com.mhl.practice.utils

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Users(

    val id: Int? = 0,
    val email: String,
    val password: String,
    val fullName: String
)