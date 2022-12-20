package com.mhl.practice.model

import com.mhl.practice.utils.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val remoteService: UsersService
){
    suspend fun authenticate(email: String, password: String): Flow<Users> {
        return flow {
            val data: Users = remoteService.authenticate(email, password)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createUser(users: Users): Flow<String>{
        return flow {
            val data = remoteService.createUser(users)
            emit(data)
        }.flowOn(Dispatchers.IO)
    }
}