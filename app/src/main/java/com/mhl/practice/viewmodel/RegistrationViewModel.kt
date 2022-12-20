package com.mhl.practice.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mhl.practice.model.LocalRepository
import com.mhl.practice.model.UsersRepository
import com.mhl.practice.utils.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @Named("authRepository") private val remoteRepository: UsersRepository
) : ViewModel() {

    val remoteData = MutableLiveData<Users>()


    suspend fun register(nickname: String, email: String, password: String, repassword: String): Boolean{
        if (password != repassword){
            return false
        }
        remoteRepository.createUser(users = Users(email = email, password = password, fullName = nickname)).collect{
            val success = it
            if (success == "Successful"){
                remoteRepository.authenticate(email, password).collect{ user ->
                    if (user.email.isNotEmpty()){
                        remoteData.value = user
                    }
                }
            }
        }

        return true

    }
}