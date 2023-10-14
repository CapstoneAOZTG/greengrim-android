package com.aoztg.greengrim.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.repository.LoginRepository
import com.aoztg.greengrim.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository : LoginRepository)  : ViewModel() {


    private var email = ""

    fun startLogin(
        token : String
    ){
        Log.d(TAG, token)
    }

    fun setEmail(_email : String){
        email = _email
    }


}