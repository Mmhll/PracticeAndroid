package com.mhl.practice.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhl.practice.model.LocalRepository
import com.mhl.practice.model.UsersRepository
import com.mhl.practice.utils.UserEntity
import com.mhl.practice.utils.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @Named("authRepository") private val repository: UsersRepository,
    @Named("localRepository") private val localRepository: LocalRepository
) : ViewModel() {

    val remoteData = MutableLiveData<Users>()



    suspend fun authorize(email: String, password: String): Boolean{
        repository.authenticate(email, password).collect{
            remoteData.value = it
            if (it.fullName.isNotEmpty()){
                localRepository.saveUser(UserEntity(
                    id = it.id!!,
                    fullName = it.fullName,
                    email = it.email,
                    password = it.password
                ))
            }
        }
        if (remoteData.value == null){
            return false
        }
        return true
    }
}