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
    suspend fun saveUser(user: UserEntity) {
        dao.deleteUser()
        dao.saveUser(user)
    }

    suspend fun getUser(): Flow<UserEntity> {
        return flow {

            var data = UserEntity(0, "", "", "")
            try {
                data = dao.getUser()
            } catch (_: NullPointerException){ }
            emit(data)
        }
    }

    suspend fun saveGrid(grid: GridEntity) = dao.saveGrid(grid)

    suspend fun deleteGrid() = dao.deleteGameInfo()

    suspend fun deleteUser() = dao.deleteUser()

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