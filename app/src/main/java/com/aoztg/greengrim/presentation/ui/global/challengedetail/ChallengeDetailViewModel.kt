package com.aoztg.greengrim.presentation.ui.global.challengedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.global.mapper.toChallengeDetail
import com.aoztg.greengrim.presentation.ui.global.model.ChallengeDetail
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
    val loading: LoadingState = LoadingState.Empty,
    val challengeDetail: ChallengeDetail = ChallengeDetail(),
)

sealed class ChallengeDetailEvents {
    object NavigateBack : ChallengeDetailEvents()
    object PopUpMenu : ChallengeDetailEvents()
    data class NavigateChatRoom(val chatId: Int, val challengeId: Int) : ChallengeDetailEvents()
    data class ShowToastMessage(val msg: String) : ChallengeDetailEvents()
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

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            challengeRepository.getChallengeDetail(challengeId).let {
                when (it) {
                    is BaseState.Success -> {
                        val uiData = it.body.toChallengeDetail()
                        _uiState.update { state ->
                            state.copy(
                                challengeDetail = uiData,
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(ChallengeDetailEvents.ShowToastMessage(it.msg))
                    }
                }
            }
            delay(500)

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(false)
                )
            }
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

    fun navigateToChatRoom(id: Int) {
        viewModelScope.launch {
            chatRepository.enterChat(id).let {
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(ChallengeDetailEvents.NavigateChatRoom(it.body.chatroomId, it.body.challengeId))
                    }

                    is BaseState.Error -> {
                        _events.emit(ChallengeDetailEvents.ShowToastMessage(it.msg))
                    }
                }
            }

        }
    }

    fun setChallengeId(data: Int) {
        challengeId = data
    }

}