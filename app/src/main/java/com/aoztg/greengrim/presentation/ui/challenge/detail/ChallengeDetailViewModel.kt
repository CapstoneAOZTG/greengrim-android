package com.aoztg.greengrim.presentation.ui.challenge.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeDetail
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
    val loading: LoadingState = LoadingState.Empty
)

sealed class ChallengeDetailEvents {
    object NavigateBack : ChallengeDetailEvents()
    object PopUpMenu : ChallengeDetailEvents()
    object RootClicked : ChallengeDetailEvents()
    data class NavigateChatRoom(val id: String) : ChallengeDetailEvents()
}

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeDetailUiState())
    val uiState: StateFlow<ChallengeDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeDetailEvents>()
    val events: SharedFlow<ChallengeDetailEvents> = _events.asSharedFlow()

    private val _challengeDetail = MutableStateFlow(ChallengeDetail())
    val challengeDetail: StateFlow<ChallengeDetail> = _challengeDetail.asStateFlow()

    fun getChallengeDetailInfo() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            _challengeDetail.value = ChallengeDetail(
                "test", "인하대학교 쓰레기 줍기",
                "함께 쓰레기를 주우며 인증하고 키워드 얻어봐요! 각자 취미에 대해 편안하게 이야기 할 수 있는 챌린지 방입니다. 인하대 후문에서 쓰레기 줍고 있습니다!",
                listOf("줍킹", "티켓 5/30"),
                listOf("인증 15회", "최소 인증 수 2회", "키워드 #줍다", "인원 15/100"),
                "2023 09-28 개설"
            )


            delay(1000)

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

    fun rootClicked() {
        viewModelScope.launch {
            _events.emit(ChallengeDetailEvents.RootClicked)
        }
    }

    fun navigateToChatRoom(id: String) {
        viewModelScope.launch {
            _events.emit(ChallengeDetailEvents.NavigateChatRoom(id))
        }
    }

}