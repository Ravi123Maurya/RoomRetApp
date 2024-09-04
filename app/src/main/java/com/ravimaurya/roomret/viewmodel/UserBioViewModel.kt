package com.ravimaurya.roomret.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.ravimaurya.roomret.MainApplication
import com.ravimaurya.roomret.approom.UserBio
import kotlinx.coroutines.launch

class UserBioViewModel(): ViewModel() {

    val userBioState = mutableStateOf(UserBioState())
    val userBioDao = MainApplication.userBioDatabase.userBioDao()

    fun onSaveClick(name: String, age: String, bio: String){
        viewModelScope.launch {
            userBioDao.insertUser(UserBio(name = name, age = age, bio = bio))
        }
    }

}


data class UserBioState(
    var userBio: List<UserBio> = emptyList(),
    val isSuccessfullyInserted: Boolean = false
)