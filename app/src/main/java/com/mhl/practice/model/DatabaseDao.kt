package com.mhl.practice.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mhl.practice.utils.GridEntity
import com.mhl.practice.utils.UserEntity

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM users")
    suspend fun getUser(): UserEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(userEntity: UserEntity)
    @Query("SELECT * FROM cells")
    suspend fun getGrid(): GridEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGrid(grid: GridEntity)
}