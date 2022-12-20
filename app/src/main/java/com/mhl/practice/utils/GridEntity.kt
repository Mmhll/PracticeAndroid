package com.mhl.practice.utils

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cells")
data class GridEntity(
    @PrimaryKey
    val id: Int = 0,
    val cells: String = "",
    val turn: Int
)