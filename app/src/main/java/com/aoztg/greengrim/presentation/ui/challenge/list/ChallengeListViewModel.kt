package com.aoztg.greengrim.presentation.ui.challenge.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.LoadingState
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

data class ChallengeListUiState(
    val loading: LoadingState = LoadingState.Empty
)

sealed class ChallengeListEvents {
    data class NavigateToChallengeDetail(val id: String) : ChallengeListEvents()
}

@HiltViewModel
class ChallengeListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeListUiState())
    val uiState: StateFlow<ChallengeListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeListEvents>()
    val events: SharedFlow<ChallengeListEvents> = _events.asSharedFlow()

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
                ChallengeRoom("test", "쓰레기 줍기", listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"), ::navigateToChallengeDetail),
                ChallengeRoom("test", "쓰레기 줍기", listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"), ::navigateToChallengeDetail),
                ChallengeRoom("test", "쓰레기 줍기", listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"), ::navigateToChallengeDetail),
                ChallengeRoom("test", "쓰레기 줍기", listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"), ::navigateToChallengeDetail),
                ChallengeRoom("test", "쓰레기 줍기", listOf("줍킹", "티켓 25/30", "인증 15회", "키워드 #줍다"), ::navigateToChallengeDetail),
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
            _events.emit(ChallengeListEvents.NavigateToChallengeDetail(id))
        }
    }
}