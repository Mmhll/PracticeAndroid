package com.mhl.practice.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mhl.practice.model.LocalRepository
import com.mhl.practice.model.UsersRepository
import com.mhl.practice.utils.UserEntity
import com.mhl.practice.utils.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    @Named("authRepository") private val repository: UsersRepository,
    @Named("localRepository") private val localRepository: LocalRepository
) : ViewModel() {

    private val remoteData = MutableLiveData<Users>()
    val isSuccessful = MutableLiveData<Boolean>()




    fun authorize(email: String, password: String) {
        viewModelScope.launch {
            repository.authenticate(email, password).collect {
                remoteData.value = it
                Log.e("USERNAME", it.toString())
                if (it.fullName.isNotEmpty()) {
                    saveUserToLocalDatabase(
                        UserEntity(
                            it.id!!,
                            it.email,
                            it.password,
                            it.fullName
                        )
                    )

                }
            }
        }
    }

    private fun saveUserToLocalDatabase(user: UserEntity) {
        viewModelScope.launch {
            localRepository.saveUser(user)
            checkAuthorize()
        }
    }

    private fun checkAuthorize() {
        isSuccessful.value = remoteData.value!!.email.isNotEmpty()
    }




}