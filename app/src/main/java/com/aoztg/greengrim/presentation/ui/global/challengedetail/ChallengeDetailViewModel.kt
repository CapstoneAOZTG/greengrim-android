package com.aoztg.greengrim.presentation.ui.global.challengedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.ChallengeRepository
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
    val challengeDetail: ChallengeDetail = ChallengeDetail()
)

sealed class ChallengeDetailEvents {
    object NavigateBack : ChallengeDetailEvents()
    object PopUpMenu : ChallengeDetailEvents()
    object RootClicked : ChallengeDetailEvents()
    data class NavigateChatRoom(val id: String) : ChallengeDetailEvents()
}

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeDetailUiState())
    val uiState: StateFlow<ChallengeDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeDetailEvents>()
    val events: SharedFlow<ChallengeDetailEvents> = _events.asSharedFlow()

    private var id = -1

    fun getChallengeDetailInfo() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            val response = challengeRepository.getChallengeDetail(id)

            if(response.isSuccessful){
                response.body()?.let{
                    val uiData = it.toChallengeDetail()
                    _uiState.update { state ->
                        state.copy(
                            challengeDetail = uiData
                        )
                    }
                }
            } else {

            }

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

    fun setId(data: Int){
        id = data
    }

}