package com.aoztg.greengrim.ui.intro.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.ui.intro.event.SignupEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(private val introRepository : IntroRepository) : ViewModel() {

    val nickname = MutableLiveData<String>()
    val introduce = MutableLiveData<String>()

    private val _eventFlow : MutableSharedFlow<SignupEvent> = MutableSharedFlow()
    val eventFlow : SharedFlow<SignupEvent> = _eventFlow


    fun postSignUp(
        nick : String,
        introduce : String,
        email : String
    ){

        viewModelScope.launch{
            // 통신로직

            // 실패시 대기

            // 성공시 To MainActivity
            _eventFlow.emit(SignupEvent.NavigateToMainActivity)
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
            _eventFlow.emit(SignupEvent.NavigateToGallery)
        }
    }

}