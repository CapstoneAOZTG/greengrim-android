package com.aoztg.greengrim.presentation.ui.info.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.ui.BaseUiState
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
    val nickState: BaseUiState = BaseUiState.Empty,
    val completeBtnState: BaseUiState = BaseUiState.Empty,
    val getProfileState: ProfileState = ProfileState.Empty,
)

sealed class ProfileState {
    object Empty : ProfileState()
    data class Success(val imgUrl: String) : ProfileState()
    object Failure : ProfileState()
    data class Error(val msg: String) : ProfileState()
}

sealed class EditProfileEvents {
    object NavigateToBack : EditProfileEvents()
    data class ShowToastMessage(val msg: String) : EditProfileEvents()
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
    val profileUrl = MutableStateFlow("")

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
            infoRepository.getProfile().let {
                when (it) {
                    is BaseState.Success -> {
                        curNickname = it.body.nickName
                        curIntroduce = it.body.introduction
                        curProfileUrl = it.body.profileImgUrl
                        nickname.value = it.body.nickName
                        introduce.value = it.body.introduction
                        profileUrl.value = it.body.profileImgUrl

                        _uiState.update { state ->
                            state.copy(getProfileState = ProfileState.Success(it.body.profileImgUrl))
                        }
                    }

                    is BaseState.Error -> {
                        _uiState.update { state ->
                            state.copy(getProfileState = ProfileState.Error(it.msg))
                        }
                    }
                }
            }
        }

        checkNickDuplicate()
        checkEditProfileState()
    }

    private fun checkNickDuplicate() {
        nickname.onEach { nick ->
            if (curNickname != nick) {
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
                            _events.emit(EditProfileEvents.ShowToastMessage(it.msg))
                        }
                    }
                }
            }

        }.launchIn(viewModelScope)
    }

    private fun checkEditProfileState() {
        isDataChanged.onEach {
            if (it) {
                _uiState.update { state ->
                    state.copy(completeBtnState = BaseUiState.Success)
                }
            } else {
                _uiState.update { state ->
                    state.copy(completeBtnState = BaseUiState.Failure)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun setProfileImg(url: String) {
        profileUrl.value = url
    }

    fun editProfile() {
        viewModelScope.launch {

            infoRepository.patchProfile(
                PatchProfileRequest(
                    nickName = nickname.value,
                    introduction = introduce.value,
                    profileImgUrl = profileUrl.value
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(EditProfileEvents.NavigateToBack)
                    }

                    is BaseState.Error -> {
                        _events.emit(EditProfileEvents.ShowToastMessage(it.msg))
                    }
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