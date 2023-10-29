package com.aoztg.greengrim.presentation.ui.challenge.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom
import com.aoztg.greengrim.presentation.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengeListUiState(
    val loading: LoadingState = LoadingState.Empty
)

@HiltViewModel
class ChallengeListViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeListUiState())
    val uiState: StateFlow<ChallengeListUiState> = _uiState.asStateFlow()

    private val _challengeRoom = MutableStateFlow<List<ChallengeRoom>>(emptyList())
    val challengeRoom: StateFlow<List<ChallengeRoom>> = _challengeRoom.asStateFlow()

    fun getChallengeRooms(){

        viewModelScope.launch{

            _uiState.update{
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            _challengeRoom.value = listOf(
                ChallengeRoom("test","title", listOf("a","b","c")),
                ChallengeRoom("test","title", listOf("a","b","c")),
                ChallengeRoom("test","title", listOf("a","b","c")),
                ChallengeRoom("test","title", listOf("a","b","c")),
                ChallengeRoom("test","title", listOf("a","b","c")),
            )

            delay(1000)

            _uiState.update{
                it.copy(
                    loading = LoadingState.IsLoading(false)
                )
            }

        }



    }
}