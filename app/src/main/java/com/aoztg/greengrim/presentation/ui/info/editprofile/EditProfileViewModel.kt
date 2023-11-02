package com.aoztg.greengrim.presentation.ui.info.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.CheckNickRequest
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.model.PatchProfileRequest
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.BaseState
import com.google.gson.Gson
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
import javax.inject.Inject

data class EditProfileUiState(
    val nickState: BaseState = BaseState.Empty,
    val completeBtnState: BaseState = BaseState.Empty,
    val getProfileState: ProfileState = ProfileState.Empty,
    val editProfileState: BaseState = BaseState.Empty
)

sealed class ProfileState {
    object Empty : ProfileState()
    data class Success(val imgUrl: String) : ProfileState()
    object Failure : ProfileState()
    data class Error(val msg: String) : ProfileState()
}

sealed class EditProfileEvents {
    object NavigateToBack : EditProfileEvents()
}

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val introRepository: IntroRepository,
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditProfileEvents>()
    val events: SharedFlow<EditProfileEvents> = _events.asSharedFlow()

    private var curNickname = ""
    private var curIntroduce = ""
    private var curProfileUrl = ""

    val nickname = MutableStateFlow("")
    val introduce = MutableStateFlow("")
    private val profileUrl = MutableStateFlow("")

    private val isNicknameValid = MutableStateFlow(false)

    val isDataChanged = combine(
        nickname,
        introduce,
        profileUrl,
        isNicknameValid
    ) { nick, introduce, profileUrl, nickValid ->
        (curNickname != nick && nickValid) || curIntroduce != introduce || profileUrl != curProfileUrl
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    init {
        getProfileData()
    }

    private fun getProfileData() {
        viewModelScope.launch {
            val response = infoRepository.getProfile()

            if (response.isSuccessful) {
                response.body()?.let {
                    curNickname = it.nickName
                    curIntroduce = it.introduction
                    curProfileUrl = it.profileImgUrl
                    nickname.value = it.nickName
                    introduce.value = it.introduction
                    profileUrl.value = it.profileImgUrl

                    _uiState.update { state ->
                        state.copy(getProfileState = ProfileState.Success(it.profileImgUrl))
                    }
                }
            } else {
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _uiState.update { state ->
                    state.copy(getProfileState = ProfileState.Error(error.message))
                }
            }
        }

        checkNickDuplicate()
        checkEditProfileState()
    }

    private fun checkNickDuplicate() {
        nickname.onEach {
            if (curNickname != it) {
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
            }

        }.launchIn(viewModelScope)
    }

    private fun checkEditProfileState() {
        isDataChanged.onEach {
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
        profileUrl.value = url
    }

    fun editProfile() {
        viewModelScope.launch {

            val response = infoRepository.patchProfile(
                PatchProfileRequest(
                    nickName = nickname.value,
                    introduction = introduce.value,
                    profileImgUrl = profileUrl.value
                )
            )

            if (response.isSuccessful) {
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

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(EditProfileEvents.NavigateToBack)
        }
    }
}