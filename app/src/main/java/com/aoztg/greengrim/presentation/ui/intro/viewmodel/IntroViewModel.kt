package com.aoztg.greengrim.presentation.ui.intro.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.LoginRequest
import com.aoztg.greengrim.data.model.SignupRequest
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.intro.event.IntroEvent
import com.aoztg.greengrim.presentation.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SignupUiState(
    val nickState: Boolean = true,
    val signupState: Boolean = false
)

@HiltViewModel
class IntroViewModel @Inject constructor(private val introRepository: IntroRepository) :
    ViewModel() {

    private val _events: MutableSharedFlow<IntroEvent> = MutableSharedFlow()
    val events: SharedFlow<IntroEvent> = _events

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private var emailData = ""

    val nickname = MutableStateFlow("")
    val introduce = MutableStateFlow("")
    val profileImg = MutableStateFlow("")

    private val isNicknameValid = MutableStateFlow(false)
    private val isDataReady = combine(nickname, isNicknameValid) { nick, nickValid ->
        nick.isNotBlank() && nickValid
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init {
        checkNickDuplicate()
        checkSignupState()
    }

    private fun checkNickDuplicate() {
        nickname.onEach {
            // 중복체크 통신부
            if(it.length == 7){
                // 성공시
                isNicknameValid.value = true
                _uiState.update { state ->
                    state.copy(nickState = true)
                }
            } else{
                // 실패시
                isNicknameValid.value = false
                _uiState.update{ state ->
                    state.copy(nickState = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun checkSignupState(){
        isDataReady.onEach{
            if(it) {
                _uiState.update{ state ->
                    state.copy(signupState = true)
                }
            } else{
                _uiState.update{ state ->
                    state.copy(signupState = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun startLogin(
        email: String
    ) {
        emailData = email
        viewModelScope.launch {
            // 통신로직
            val response = introRepository.login(
                LoginRequest(
                    email = email
                )
            )

            if (response.isSuccessful) {
                _events.emit(IntroEvent.NavigateToMainActivity)
            } else {
                val code = response.code()
                val body = response.errorBody()?.string()
                body?.let {
                    Log.d(Constants.TAG, code.toString())
                    Log.d(Constants.TAG, it)
                }
                _events.emit(IntroEvent.NavigateToTermsFragment)
            }
        }
    }

    fun navigateToSignup() {
        viewModelScope.launch {
            _events.emit(IntroEvent.NavigateToSignupFragment)
        }
    }

    fun goToGallery() {
        viewModelScope.launch {
            _events.emit(IntroEvent.NavigateToGallery)
        }
    }

    fun signUp() {
        viewModelScope.launch {
            // 통신로직
            val response = introRepository.signup(
                SignupRequest(
                    email = emailData,
                    nickName = nickname.value,
                    introduction = introduce.value,
                    profileImgUrl = profileImg.value
                )
            )

            if (response.isSuccessful) {
                isNicknameValid.value = true
                _events.emit(IntroEvent.NavigateToMainActivity)
            } else {
                val code = response.code()
                val body = response.errorBody()?.string()
                body?.let {
                    Log.d(Constants.TAG, code.toString())
                    Log.d(Constants.TAG, it)
                }
            }
        }
    }

}