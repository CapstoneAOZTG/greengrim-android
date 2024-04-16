package com.aoztg.greengrim.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
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
    data class ShowSnackMessage(val msg: String) : MySettingEvent()
}

@HiltViewModel
class MySettingViewModel @Inject constructor(
    private val repository : MemberRepository
): ViewModel() {

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
            repository.logout().let{
                when(it){
                    is BaseState.Success -> _event.emit(MySettingEvent.Logout)
                    is BaseState.Error -> _event.emit(MySettingEvent.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    fun withDraw(){
        viewModelScope.launch {
            repository.withdraw().let{
                when(it){
                    is BaseState.Success -> _event.emit(MySettingEvent.WithDraw)
                    is BaseState.Error -> _event.emit(MySettingEvent.ShowSnackMessage(it.msg))
                }
            }

        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(MySettingEvent.NavigateToBack)
        }
    }
}