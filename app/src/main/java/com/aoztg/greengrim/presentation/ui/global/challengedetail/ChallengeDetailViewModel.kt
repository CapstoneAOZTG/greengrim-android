package com.aoztg.greengrim.presentation.ui.global.challengedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiChallengeDetail
import com.aoztg.greengrim.presentation.ui.global.model.UiChallengeDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeDetailUiState(
    val uiChallengeDetail: UiChallengeDetail = UiChallengeDetail(),
)

sealed class ChallengeDetailEvents {
    object NavigateBack : ChallengeDetailEvents()
    object PopUpMenu : ChallengeDetailEvents()
    data class NavigateChatRoom(val chatId: Int, val challengeId: Int) : ChallengeDetailEvents()
    data class ShowToastMessage(val msg: String) : ChallengeDetailEvents()
    data class ShowSnackMessage(val msg: String) : ChallengeDetailEvents()
    object ShowLoading : ChallengeDetailEvents()
    object DismissLoading : ChallengeDetailEvents()
}

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeDetailUiState())
    val uiState: StateFlow<ChallengeDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeDetailEvents>()
    val events: SharedFlow<ChallengeDetailEvents> = _events.asSharedFlow()

    private var challengeId = -1

    fun getChallengeDetailInfo() {
        viewModelScope.launch {

            _events.emit(ChallengeDetailEvents.ShowLoading)

            challengeRepository.getChallengeDetail(challengeId).let {
                when (it) {
                    is BaseState.Success -> {
                        val uiData = it.body.toUiChallengeDetail()
                        _uiState.update { state ->
                            state.copy(
                                uiChallengeDetail = uiData,
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(ChallengeDetailEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
            delay(500)
            _events.emit(ChallengeDetailEvents.DismissLoading)
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(ChallengeDetailEvents.NavigateBack)
        }
    }

    fun popUpMenu() {
        viewModelScope.launch {
            _events.emit(ChallengeDetailEvents.PopUpMenu)
        }
    }

    fun enterChat(id: Int) {
        viewModelScope.launch {

            _events.emit(ChallengeDetailEvents.ShowLoading)

            chatRepository.enterChat(id).let {
                _events.emit(ChallengeDetailEvents.DismissLoading)
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(ChallengeDetailEvents.ShowToastMessage("채팅방에 입장했습니다"))
                        _events.emit(
                            ChallengeDetailEvents.NavigateChatRoom(
                                it.body.chatroomId,
                                it.body.challengeId
                            )
                        )
                    }

                    is BaseState.Error -> {
                        _events.emit(ChallengeDetailEvents.ShowSnackMessage(it.msg))
                    }
                }
            }

        }
    }

    fun setChallengeId(data: Int) {
        challengeId = data
    }

}