package com.aoztg.greengrim.presentation.ui.intro.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.app.App.Companion.sharedPreferences
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.BaseUiState
import com.aoztg.greengrim.presentation.ui.challenge.create.CreateChallengeDetailEvents
import com.aoztg.greengrim.presentation.ui.intro.EmailData
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.X_ACCESS_TOKEN
import com.aoztg.greengrim.presentation.util.Constants.X_REFRESH_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


data class SignupUiState(
    val nickState: BaseUiState = BaseUiState.Empty,
    val nextBtnState: BaseUiState = BaseUiState.Empty,
    val signupState: BaseUiState = BaseUiState.Empty
)

sealed class SignupEvents {
    object ShowLoading : SignupEvents()
    object DismissLoading : SignupEvents()
    data class ShowSnackMessage(val msg: String) : SignupEvents()
    data class ShowToastMessage(val msg: String) : SignupEvents()
}

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val imageRepository: ImageRepository
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SignupEvents>()
    val events: SharedFlow<SignupEvents> = _events.asSharedFlow()

    val nickname = MutableStateFlow("")
    val introduce = MutableStateFlow("")
    private val isNicknameValid = MutableStateFlow(false)
    val isImageSet = MutableStateFlow(false)
    private var imgFile: MultipartBody.Part? = null

    private val isDataReady = combine(nickname, isNicknameValid) { nick, nickValid ->
        nick.isNotBlank() && nickValid
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setImageFile(
        file: MultipartBody.Part
    ) {
        isImageSet.value = true
        imgFile = file
    }

    init {
        checkNickDuplicate()
        checkSignupState()
    }

    private fun checkNickDuplicate() {
        nickname.onEach { nick ->
            introRepository.checkNick(CheckNickRequest(nick)).let {
                when (it) {
                    is BaseState.Success -> {
                        if (it.body.used) {
                            isNicknameValid.value = false
                            _uiState.update { state ->
                                state.copy(nickState = BaseUiState.Error("사용할 수 없는 닉네임 입니다"))
                            }
                        } else {
                            isNicknameValid.value = true
                            _uiState.update { state ->
                                state.copy(nickState = BaseUiState.Success)
                            }
                        }
                    }

                    is BaseState.Error -> {
                        isNicknameValid.value = false
                        _uiState.update { state ->
                            state.copy(nickState = BaseUiState.Error(it.msg))
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun checkSignupState() {
        isDataReady.onEach {
            if (it) {
                _uiState.update { state ->
                    state.copy(nextBtnState = BaseUiState.Success)
                }
            } else {
                _uiState.update { state ->
                    state.copy(nextBtnState = BaseUiState.Failure)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun imageToUrl() {
        viewModelScope.launch {
            _events.emit(SignupEvents.ShowLoading)

            imgFile?.let { img ->
                imageRepository.imageToUrl(img).let {
                    when (it) {
                        is BaseState.Success -> {
                            signUp(it.body.imgUrl)
                        }

                        is BaseState.Error -> {
                            _events.emit(SignupEvents.ShowSnackMessage(it.msg))
                            _events.emit(SignupEvents.DismissLoading)
                        }
                    }
                }
            } ?: run {
                signUp("")
            }
        }
    }

    fun signUp(imgUrl: String) {
        viewModelScope.launch {
            // 통신로직
            introRepository.signup(
                SignupRequest(
                    email = EmailData.email,
                    nickName = nickname.value,
                    introduction = introduce.value,
                    profileImgUrl = imgUrl,
                    App.fcmToken
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        sharedPreferences.edit()
                            .putString(X_ACCESS_TOKEN, it.body.accessToken)
                            .putString(X_REFRESH_TOKEN, it.body.refreshToken)
                            .putLong(Constants.MEMBER_ID, it.body.memberId)
                            .apply()

                        _events.emit(SignupEvents.ShowToastMessage("회원가입 완료!"))
                        _uiState.update { state ->
                            state.copy(signupState = BaseUiState.Success)
                        }
                    }

                    is BaseState.Error -> {
                        _uiState.update { state ->
                            state.copy(signupState = BaseUiState.Error(it.msg))
                        }
                    }
                }
            }
        }
    }

}