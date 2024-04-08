package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListEvents
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

data class HotChallengeListUiState(
    val uiChallengeRoom: List<UiChallengeRoom> = emptyList(),
    val sortType: HotChallengeSortType = HotChallengeSortType.MOST_RECENT,
    val page: Int = 0,
    val hasNext: Boolean = true
)

sealed class HotChallengeListEvents {
    data class NavigateToChallengeDetail(val id: Long) : HotChallengeListEvents()
    data class ShowSnackMessage(val msg: String) : HotChallengeListEvents()
    object ShowLoading : HotChallengeListEvents()
    object DismissLoading : HotChallengeListEvents()
}

@HiltViewModel
class HotChallengeListViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    companion object{
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(HotChallengeListUiState())
    val uiState: StateFlow<HotChallengeListUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HotChallengeListEvents>()
    val event: SharedFlow<HotChallengeListEvents> = _event.asSharedFlow()

    fun getHotChallengeList(option : Int){
        if(uiState.value.hasNext){
            viewModelScope.launch {
                _event.emit(HotChallengeListEvents.ShowLoading)

                challengeRepository.getMoreHotChallenges(
                    uiState.value.sortType.value,
                    uiState.value.page,
                    20
                ).let{
                    when(it){
                        is BaseState.Success -> {
                            val uiData = it.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = if(option == ORIGINAL) uiState.value.uiChallengeRoom + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1
                                )
                            }
                        }

                        is BaseState.Error -> _event.emit(HotChallengeListEvents.ShowSnackMessage(it.msg))
                    }
                    delay(500)
                    _event.emit(HotChallengeListEvents.DismissLoading)
                }
            }
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _event.emit(HotChallengeListEvents.NavigateToChallengeDetail(id))
        }
    }

}

enum class HotChallengeSortType(val value: String){
    MOST_RECENT("MOST_RECENT"),
    MOST_HEADCOUNT("MOST_HEADCOUNT"),
    MOST_CERTIFICATION("MOST_CERTIFICATION")
}
