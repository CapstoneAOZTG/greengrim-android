package com.aoztg.greengrim.presentation.ui.info.mychallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom
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

data class MyChallengeUiState(
    val loading: LoadingState = LoadingState.Empty
)

sealed class MyChallengeEvents {
    data class NavigateToChallengeDetail(val id: String) : MyChallengeEvents()
    object ShowBottomSheet : MyChallengeEvents()
}

@HiltViewModel
class MyChallengeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MyChallengeUiState())
    val uiState: StateFlow<MyChallengeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyChallengeEvents>()
    val events: SharedFlow<MyChallengeEvents> = _events.asSharedFlow()

    private val _challengeRoom = MutableStateFlow<List<ChallengeRoom>>(emptyList())
    val challengeRoom: StateFlow<List<ChallengeRoom>> = _challengeRoom.asStateFlow()

    fun getChallengeRooms() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            _challengeRoom.value = listOf(
                ChallengeRoom(
                    "test",
                    "쓰레기 줍기",
                    listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"),
                    ::navigateToChallengeDetail
                ),
                ChallengeRoom(
                    "test",
                    "쓰레기 줍기",
                    listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"),
                    ::navigateToChallengeDetail
                ),
                ChallengeRoom(
                    "test",
                    "쓰레기 줍기",
                    listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"),
                    ::navigateToChallengeDetail
                ),
                ChallengeRoom(
                    "test",
                    "쓰레기 줍기",
                    listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"),
                    ::navigateToChallengeDetail
                ),
                ChallengeRoom(
                    "test",
                    "쓰레기 줍기",
                    listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"),
                    ::navigateToChallengeDetail
                ),
            )

            delay(1000)

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(false)
                )
            }
        }
    }

    private fun navigateToChallengeDetail(id: String) {
        viewModelScope.launch {
            _events.emit(MyChallengeEvents.NavigateToChallengeDetail(id))
        }
    }

    fun showBottomSheet(){
        viewModelScope.launch{
            _events.emit(MyChallengeEvents.ShowBottomSheet)
        }
    }

    fun setSortType(type: ChallengeSortType){

    }
}
