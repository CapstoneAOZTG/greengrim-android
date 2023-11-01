package com.aoztg.greengrim.presentation.ui.info.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.model.CheckNickRequest
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.model.SignupRequest
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.intro.EmailData
import com.aoztg.greengrim.presentation.util.Constants
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

data class EditProfileUiState(
    val nickState: BaseState = BaseState.Empty,
    val completeBtnState: BaseState = BaseState.Empty,
    val editProfileState: BaseState = BaseState.Empty
)

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val introRepository: IntroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    val nickname = MutableStateFlow("")
    val introduce = MutableStateFlow("")
    private var profileUrl = ""

    private val isNicknameValid = MutableStateFlow(false)
    private val isDataReady = combine(nickname, isNicknameValid) { nick, nickValid ->
        nick.isNotBlank() && nickValid
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init {
        getProfileData()
    }

    private fun getProfileData() {
        //todo 프로필 데이터를 셋팅 한 뒤에, State 리스너를 등록한다

        checkNickDuplicate()
        checkSignupState()
    }

    private fun checkNickDuplicate() {
        nickname.onEach {
            val response = introRepository.checkNick(CheckNickRequest(it))
            if (response.isSuccessful) {
                response.body()?.let { body ->

                    if (body.used) {
                        isNicknameValid.value = false
                        _uiState.update { state ->
                            state.copy(nickState = BaseState.Error("사용할 수 없는 닉네임 입니다"))
                        }
                    } else {
                        isNicknameValid.value = true
                        _uiState.update { state ->
                            state.copy(nickState = BaseState.Success)
                        }
                    }
                }
            } else {
                isNicknameValid.value = false
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _uiState.update { state ->
                    state.copy(nickState = BaseState.Error(error.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun checkSignupState() {
        isDataReady.onEach {
            if (it) {
                _uiState.update { state ->
                    state.copy(completeBtnState = BaseState.Success)
                }
            } else {
                _uiState.update { state ->
                    state.copy(completeBtnState = BaseState.Failure)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setProfileImg(url: String) {
        profileUrl = url
    }

    fun editProfile() {
        
        // todo signup API -> Edit Profile 로 수정
        
        viewModelScope.launch {
            // 통신로직
            val response = introRepository.signup(
                SignupRequest(
                    email = EmailData.email,
                    nickName = nickname.value,
                    introduction = introduce.value,
                    profileImgUrl = profileUrl
                )
            )

            if (response.isSuccessful) {
                isNicknameValid.value = true

                response.body()?.let {
                    App.sharedPreferences.edit()
                        .putString(Constants.X_ACCESS_TOKEN, "Bearer " + it.accessToken)
                        .putString(Constants.X_REFRESH_TOKEN, it.refreshToken)
                        .apply()
                }

                _uiState.update { state ->
                    state.copy(editProfileState = BaseState.Success)
                }
            } else {
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _uiState.update { state ->
                    state.copy(editProfileState = BaseState.Error(error.message))
                }
            }
        }
    }
}