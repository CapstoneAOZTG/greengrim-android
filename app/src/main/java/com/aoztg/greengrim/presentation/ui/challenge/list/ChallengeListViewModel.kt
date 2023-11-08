package com.aoztg.greengrim.presentation.ui.challenge.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toChallengeListData
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
    object NavigateToCreateChallenge : ChallengeListEvents()
    object ShowBottomSheet : ChallengeListEvents()
}

@HiltViewModel
class ChallengeListViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeListUiState())
    val uiState: StateFlow<ChallengeListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeListEvents>()
    val events: SharedFlow<ChallengeListEvents> = _events.asSharedFlow()

    private val _challengeRoom = MutableStateFlow<List<ChallengeRoom>>(emptyList())
    val challengeRoom: StateFlow<List<ChallengeRoom>> = _challengeRoom.asStateFlow()

    private var sorType = ChallengeSortType.DESC
    private var category = ""

    var page = 0
    var hasNext = false

    fun getChallengeList() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    loading = LoadingState.IsLoading(true)
                )
            }

            val response = challengeRepository.getChallengeList(category,0,20,sorType.value)

            if(response.isSuccessful){
                response.body()?.let{ body ->
                    val uiData = body.toChallengeListData(::navigateToChallengeDetail)
                    hasNext = uiData.hasNext
                    page = uiData.page
                    _challengeRoom.value = uiData.result
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

    private fun navigateToChallengeDetail(id: String) {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToChallengeDetail(id))
        }
    }

    fun navigateToCreateChallenge(){
        viewModelScope.launch{
            _events.emit(ChallengeListEvents.NavigateToCreateChallenge)
        }
    }

    fun showBottomSheet(){
        viewModelScope.launch{
            _events.emit(ChallengeListEvents.ShowBottomSheet)
        }
    }

    fun setSortType(type: ChallengeSortType){
        sorType = type
    }

    fun setCategory(type: String){
        category = type
    }
}

enum class ChallengeSortType(val text: String, val value: String){
    DESC("최신순", "DESC"),
    ASC("오래된 순", "ASC"),
    GREATEST("인원 많은 순", "GREATEST"),
    LEAST("인원 적은 순", "LEAST")
}