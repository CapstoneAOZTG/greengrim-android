package com.aoztg.greengrim.presentation.ui.intro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.intro.event.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val introRepository : IntroRepository)  : ViewModel() {


    private val _eventFlow : MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val eventFlow : SharedFlow<LoginEvent> = _eventFlow


    fun startLogin(
        token : String
    ){
        viewModelScope.launch{
            // 통신로직

            // 실패시 To TermsActivity 하고 회원가입 진행
            _eventFlow.emit(LoginEvent.NavigateToTermsActivity)

//            // 성공시 To MainActivity
//            _navigationHandler.emit(LoginNavigationAction.NavigateToMainActivity)
        }
    }

}