package com.aoztg.greengrim.presentation.ui.intro.viewmodel

import androidx.lifecycle.*
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.intro.event.SignupEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(private val introRepository : IntroRepository) : ViewModel() {

    val nickname = MutableLiveData<String>()
    val introduce = MutableLiveData<String>()
    private val isNicknameValid = MutableLiveData(false)

    private val _isAllDataSet = MediatorLiveData<Boolean>().apply{
        value = false
    }
    val isAllDataSet : LiveData<Boolean> = _isAllDataSet

    private val _eventFlow : MutableSharedFlow<SignupEvent> = MutableSharedFlow()
    val eventFlow : SharedFlow<SignupEvent> = _eventFlow


    init{
        with(_isAllDataSet){
            addSource(nickname){
                checkData()
            }
            addSource(introduce){
                checkData()
            }
        }
    }

    private fun checkData(){
        _isAllDataSet.value = (!nickname.value.isNullOrBlank() && !introduce.value.isNullOrBlank() && isNicknameValid.value == true)

    }

    fun postSignUp(){

//        val data = SignupPostData(EmailData.email,nickname.value, introduce.value)

        viewModelScope.launch{
            // 통신로직

            // 실패시 대기

            // 성공시 To MainActivity
            _eventFlow.emit(SignupEvent.NavigateToMainActivity)
            isNicknameValid.value = true
        }
    }


    fun checkNick(
        nick : String
    ){
        viewModelScope.launch{
            // 통신로직

            // 실패시 대기

            // 성공시 분기처리
            isNicknameValid.value = true
        }
    }

    fun goToGallery(){
        viewModelScope.launch{
            _eventFlow.emit(SignupEvent.NavigateToGallery)
        }
    }

}