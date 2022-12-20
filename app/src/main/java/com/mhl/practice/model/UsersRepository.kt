package com.mhl.practice.model

import com.mhl.practice.utils.Users
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val remoteService: UsersService
){
    suspend fun authenticate(email: String, password: String): Flow<Users> {
        return flow {
            var data = Users(id = 0, email = "", password = "", fullName = "")
            try {
                data = remoteService.authenticate(email, password)
            } catch (_: HttpException){
            }
            emit(data)
        }
    }

    suspend fun createUser(users: Users): Flow<ResponseBody>{
        return flow {
            val data = remoteService.createUser(users)
            emit(data)
        }
    }
}