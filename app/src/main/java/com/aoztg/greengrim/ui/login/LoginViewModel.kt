package com.aoztg.greengrim.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.LoginRepository
import com.aoztg.greengrim.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository : LoginRepository)  : ViewModel() {


    private var email = ""

    private val _navigationHandler : MutableSharedFlow<LoginNavigationAction> = MutableSharedFlow<LoginNavigationAction>()
    val navigationHandler : SharedFlow<LoginNavigationAction> = _navigationHandler


    fun startLogin(
        token : String
    ){
        Log.d(TAG,token)
        viewModelScope.launch{
            // 통신로직

            // 실패시 To TermsActivity 하고 회원가입 진행
            _navigationHandler.emit(LoginNavigationAction.NavigateToTermsActivity(email))

//            // 성공시 To MainActivity
//            _navigationHandler.emit(LoginNavigationAction.NavigateToMainActivity)
        }

    }

    fun setEmail(_email : String){
        email = _email
    }


}