package com.aoztg.greengrim.presentation.ui.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.presentation.ui.mypage.mapper.toUiMyInfo
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MyPageUiState(
    val uiMyInfo: UiMyInfo = UiMyInfo()
)

sealed class MyPageEvent {
    object ShowBottomSheet : MyPageEvent()
    object GoToIntroActivity : MyPageEvent()
    object NavigateToAttendCheck : MyPageEvent()
    object NavigateToMyChallenge : MyPageEvent()
    object NavigateToMyCertification : MyPageEvent()
    object NavigateToMyWallet : MyPageEvent()
    object NavigateToMyNft : MyPageEvent()
    data class ShowToastMessage(val msg: String) : MyPageEvent()
    data class ShowSnackMessage(val msg: String) : MyPageEvent()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageUiState())
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyPageEvent>()
    val events: SharedFlow<MyPageEvent> = _events.asSharedFlow()

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(MyPageEvent.ShowBottomSheet)
        }
    }

    fun getMyInfo() {
        viewModelScope.launch {
            infoRepository.getMyInfo().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiMyInfo = it.body.toUiMyInfo()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(MyPageEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun withdrawal() {
        viewModelScope.launch {
            infoRepository.withdrawal().let {
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(MyPageEvent.GoToIntroActivity)
                    }

                    is BaseState.Error -> {
                        _events.emit(MyPageEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToAttendCheck() {
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToAttendCheck)
        }
    }

    fun navigateToMyCertification() {
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToMyCertification)
        }
    }

    fun navigateToMyChallenge() {
        viewModelScope.launch {
            _events.emit(MyPageEvent.NavigateToMyChallenge)
        }
    }

    fun navigateToMyNft() {
        viewModelScope.launch {
            if (uiState.value.uiMyInfo.hasWallet) {
                _events.emit(MyPageEvent.NavigateToMyNft)
            } else {
                _events.emit(MyPageEvent.ShowSnackMessage("지갑을 먼저 생성해주세요!"))
            }
        }
    }
}
