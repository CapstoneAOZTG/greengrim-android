package com.aoztg.greengrim.presentation.ui.challenge.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.customview.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toUiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
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
    val uiChallengeRoom: List<UiChallengeRoom> = emptyList(),
    val challengeSortType: ChallengeSortType = ChallengeSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class ChallengeListEvents {
    data class NavigateToChallengeDetail(val id: Long) : ChallengeListEvents()
    object NavigateToCreateChallenge : ChallengeListEvents()
    object NavigateToSearchChallenge : ChallengeListEvents()
    object ShowBottomSheet : ChallengeListEvents()
    object ScrollToTop : ChallengeListEvents()
    data class ShowSnackMessage(val msg: String) : ChallengeListEvents()
    object ShowLoading : ChallengeListEvents()
    object DismissLoading : ChallengeListEvents()
    object NavigateToAlarmCheck: ChallengeListEvents()
}

@HiltViewModel
class ChallengeListViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(ChallengeListUiState())
    val uiState: StateFlow<ChallengeListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChallengeListEvents>()
    val events: SharedFlow<ChallengeListEvents> = _events.asSharedFlow()

    private var category = ""

    fun getChallengeList(option: Int) {

        if (uiState.value.hasNext) {
            viewModelScope.launch {

                challengeRepository.getChallengeList(
                    category,
                    uiState.value.page,
                    20,
                    uiState.value.challengeSortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData = it.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = if (option == ORIGINAL) uiState.value.uiChallengeRoom + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                )
                            }

                            delay(100)
                            _events.emit(ChallengeListEvents.ScrollToTop)
                        }

                        is BaseState.Error -> _events.emit(ChallengeListEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToChallengeDetail(id))
        }
    }

    fun navigateToCreateChallenge() {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToCreateChallenge)
        }
    }

    fun navigateToSearchChallenge() {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToSearchChallenge)
        }
    }

    fun navigateToAlarmCheck(){
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToAlarmCheck)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.ShowBottomSheet)
        }
    }

    fun setSortType(type: ChallengeSortType) {
        _uiState.value = _uiState.value.copy(
            hasNext = true,
            challengeSortType = type,
            page = 0
        )
        getChallengeList(SORT)
    }

    fun setCategory(type: String) {
        category = type
    }
}

