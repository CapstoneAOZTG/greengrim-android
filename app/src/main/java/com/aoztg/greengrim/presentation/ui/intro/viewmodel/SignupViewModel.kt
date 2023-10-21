package com.aoztg.greengrim.presentation.ui.intro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.model.SignupRequest
import com.aoztg.greengrim.data.repository.IntroRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    val nickState: SignupState = SignupState.Empty,
    val nextBtnState: SignupState = SignupState.Empty,
    val signupState: SignupState = SignupState.Empty
)
sealed class SignupState {
    object Empty : SignupState()
    object Success : SignupState()
    object Failure : SignupState()
    data class Error(val msg: String) : SignupState()
}

@HiltViewModel
class SignupViewModel @Inject constructor(private val introRepository: IntroRepository) :
    ViewModel() {

    companion object {
        const val UNAVAILABLE_EMAIL = "GLOBAL_001"
        const val UNAVAILABLE_MEMEBER = "MEMBER_001"
        const val DUPLICATE_MEMEBER = "MEMBER_002"
        const val DUPLICATE_NICK = "MEMBER_003"
        const val UNREGISTERED_EMAIL = "MEMBER_004"
    }

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    val nickname = MutableStateFlow("")
    val introduce = MutableStateFlow("")
    private var profileUrl = ""
    private var emailData = ""


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
            if (it.length == 7) {
                // 성공시
                isNicknameValid.value = true
                _uiState.update { state ->
                    state.copy(nickState = SignupState.Success)
                }
            } else {
                // 실패시
                isNicknameValid.value = false
                _uiState.update { state ->
                    state.copy(nickState = SignupState.Error("에러에러에러에러"))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun checkSignupState() {
        isDataReady.onEach {
            if (it) {
                _uiState.update { state ->
                    state.copy(nextBtnState = SignupState.Success)
                }
            } else {
                _uiState.update { state ->
                    state.copy(nextBtnState = SignupState.Failure)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setProfileImg(url: String) {
        profileUrl = url
    }

    fun signUp() {
        viewModelScope.launch {
            // 통신로직
            val response = introRepository.signup(
                SignupRequest(
                    email = emailData,
                    nickName = nickname.value,
                    introduction = introduce.value,
                    profileImgUrl = profileUrl
                )
            )

            if (response.isSuccessful) {
                isNicknameValid.value = true
                _uiState.update { state ->
                    state.copy(signupState = SignupState.Success)
                }
            } else {
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _uiState.update { state ->
                    state.copy(signupState = SignupState.Error(error.message))
                }
            }
        }
    }

}