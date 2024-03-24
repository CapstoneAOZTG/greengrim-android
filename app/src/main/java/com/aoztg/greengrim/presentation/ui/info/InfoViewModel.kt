package com.aoztg.greengrim.presentation.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.presentation.ui.info.mapper.toUiMyInfo
import com.aoztg.greengrim.presentation.ui.info.model.UiMyInfo
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


data class InfoUiState(
    val uiMyInfo: UiMyInfo = UiMyInfo()
)

sealed class InfoEvents {
    object ShowBottomSheet : InfoEvents()
    object GoToIntroActivity : InfoEvents()
    object NavigateToAttendCheck : InfoEvents()
    object NavigateToMyChallenge : InfoEvents()
    object NavigateToMyCertification : InfoEvents()
    object NavigateToMyWallet : InfoEvents()
    object NavigateToMyNft : InfoEvents()
    data class ShowToastMessage(val msg: String) : InfoEvents()
    data class ShowSnackMessage(val msg: String) : InfoEvents()
}

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val infoRepository: InfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InfoUiState())
    val uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<InfoEvents>()
    val events: SharedFlow<InfoEvents> = _events.asSharedFlow()

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(InfoEvents.ShowBottomSheet)
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
                        _events.emit(InfoEvents.ShowSnackMessage(it.msg))
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
                        _events.emit(InfoEvents.GoToIntroActivity)
                    }

                    is BaseState.Error -> {
                        _events.emit(InfoEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToAttendCheck() {
        viewModelScope.launch {
            _events.emit(InfoEvents.NavigateToAttendCheck)
        }
    }

    fun navigateToMyCertification() {
        viewModelScope.launch {
            _events.emit(InfoEvents.NavigateToMyCertification)
        }
    }

    fun navigateToMyChallenge() {
        viewModelScope.launch {
            _events.emit(InfoEvents.NavigateToMyChallenge)
        }
    }

    fun navigateToMyNft(){
        viewModelScope.launch {
            if(uiState.value.uiMyInfo.hasWallet){
                _events.emit(InfoEvents.NavigateToMyNft)
            } else {
                _events.emit(InfoEvents.ShowSnackMessage("지갑을 먼저 생성해주세요!"))
            }
        }
    }
}
