package com.mhl.practice.model

import com.mhl.practice.utils.GridEntity
import com.mhl.practice.utils.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val dao: DatabaseDao
) {
    suspend fun saveUser(user: UserEntity) = dao.saveUser(user)

    suspend fun getUser(): Flow<UserEntity> {
        return flow {
            val data = dao.getUser()
            emit(data)
        }
    }

    suspend fun saveGrid(grid: GridEntity) = dao.saveGrid(grid)

    suspend fun getGrid(): Flow<GridEntity> {
        return flow {
            val data: GridEntity = try {
                dao.getGrid()
            } catch (_: Exception) {
                GridEntity(0, "", 0)
            }
            emit(data)
        }.flowOn(Dispatchers.Default)
    }

}