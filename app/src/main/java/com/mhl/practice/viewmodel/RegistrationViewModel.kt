package com.mhl.practice.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhl.practice.model.UsersRepository
import com.mhl.practice.utils.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    @Named("authRepository") private val remoteRepository: UsersRepository
) : ViewModel() {

    private var successful: String = ""
    val isSuccessful = MutableLiveData<Boolean>()


    private fun checkRegistration(
    ) {
        when (successful) {
            "Successful" -> isSuccessful.value = true
            else -> isSuccessful.value = false
        }
    }

    fun registration(
        nickname: String, email: String, password: String,
    ) {
        viewModelScope.launch {
            remoteRepository.createUser(
                users = Users(
                    email = email, password = password, fullName = nickname
                )
            ).collect {
                successful = it.string()
            }
            checkRegistration()
        }
    }
}