package com.mhl.practice.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mhl.practice.utils.GridEntity
import com.mhl.practice.utils.UserEntity

@Database(entities = [UserEntity::class, GridEntity::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun getDao(): DatabaseDao
}