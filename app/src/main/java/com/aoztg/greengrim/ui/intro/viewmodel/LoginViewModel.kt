package com.aoztg.greengrim.ui.intro.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.ui.intro.event.LoginEvent
import com.aoztg.greengrim.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val introRepository : IntroRepository)  : ViewModel() {


    private var email = ""

    private val _eventFlow : MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val eventFlow : SharedFlow<LoginEvent> = _eventFlow


    fun startLogin(
        token : String
    ){
        Log.d(Constants.TAG, "$email $token")
        viewModelScope.launch{
            // 통신로직

            // 실패시 To TermsActivity 하고 회원가입 진행
            _eventFlow.emit(LoginEvent.NavigateToTermsActivity(email))

//            // 성공시 To MainActivity
//            _navigationHandler.emit(LoginNavigationAction.NavigateToMainActivity)
        }
    }



    fun setEmail(_email : String){
        email = _email
    }
}