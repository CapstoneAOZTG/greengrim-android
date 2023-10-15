package com.aoztg.greengrim.ui.intro.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.network.loginnetwork.model.SignupPostData
import com.aoztg.greengrim.data.repository.IntroRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class SignupViewModel @Inject constructor(private val introRepository : IntroRepository) : ViewModel() {

    val nickname = MutableLiveData<String>()
    val introduce = MutableLiveData<String>()

    private val _navigationHandler : MutableSharedFlow<SignupNavigationAction> = MutableSharedFlow()
    val navigationHandler : SharedFlow<SignupNavigationAction> = _navigationHandler


    fun postSignUp(
        nick : String,
        introduce : String,
        email : String
    ){

        viewModelScope.launch{
            // 통신로직

            // 실패시 대기

            // 성공시 To MainActivity
            _navigationHandler.emit(SignupNavigationAction.NavigateToMainActivity)
        }
    }

    fun checkNick(
        nick : String
    ){
        viewModelScope.launch{
            // 통신로직

            // 실패시 대기

            // 성공시 분기처리

        }
    }

    fun goToGallery(){
        viewModelScope.launch{
            _navigationHandler.emit(SignupNavigationAction.NavigateToGallery)
        }
    }

}