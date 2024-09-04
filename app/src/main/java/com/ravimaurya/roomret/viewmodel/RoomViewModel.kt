package com.ravimaurya.roomret.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ravimaurya.roomret.MainApplication
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class RoomViewModel: ViewModel() {


    val userBioDao = MainApplication.userBioDatabase.userBioDao()
   private val _userBioState = mutableStateOf(UserBioState())
    val userBioState: State<UserBioState> = _userBioState

    init {
        getAllUserBio()
    }

    fun getAllUserBio(){
        viewModelScope.launch {

        _userBioState.value = userBioState.value.copy(
            userBio = userBioDao.getAllUserBio()
        )
        }
    }
    fun deleteUser(id: Int){
        viewModelScope.launch {
            userBioDao.deleteUserBio(id)
        }
    }

}