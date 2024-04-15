package com.aoztg.greengrim.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MySettingEvent{
    object NavigateToEditProfile : MySettingEvent()
    object NavigateToSetWallet : MySettingEvent()
    object NavigateToEditAlarm : MySettingEvent()
    object NavigateToBack : MySettingEvent()
    object Logout : MySettingEvent()
    object WithDraw : MySettingEvent()
}

@HiltViewModel
class MySettingViewModel @Inject constructor(): ViewModel() {

    private val _event = MutableSharedFlow<MySettingEvent>()
    val event : SharedFlow<MySettingEvent> = _event.asSharedFlow()


    fun navigateToEditProfile(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.NavigateToEditProfile)
        }
    }

    fun navigateToSetWallet(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.NavigateToSetWallet)
        }
    }

    fun navigateToEditAlarm(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.NavigateToEditAlarm)
        }
    }

    fun logout(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.Logout)
        }
    }

    fun withDraw(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.WithDraw)
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.NavigateToBack)
        }
    }
}