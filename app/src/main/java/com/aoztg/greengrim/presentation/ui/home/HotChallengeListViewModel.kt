package com.aoztg.greengrim.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.customview.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListEvents
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toUiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
import com.aoztg.greengrim.presentation.ui.global.profile.ProfileViewModel
import com.aoztg.greengrim.presentation.ui.mypage.myprofile.ProfileFilter
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
    val curFilter: HotChallengeSortType = HotChallengeSortType.MOST_CERTIFICATION,
    val page: Int = 0,
    val hasNext: Boolean = true
)

sealed class HotChallengeListEvents {
    data class NavigateToChallengeDetail(val id: Long) : HotChallengeListEvents()
    data class ShowSnackMessage(val msg: String) : HotChallengeListEvents()
    object NavigateToChallengeCategory: HotChallengeListEvents()
    object ShowLoading : HotChallengeListEvents()
    object DismissLoading : HotChallengeListEvents()
    object ScrollToTop : HotChallengeListEvents()
    object NavigateToBack : HotChallengeListEvents()
}

@HiltViewModel
class HotChallengeListViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val _uiState = MutableStateFlow(HotChallengeListUiState())
    val uiState: StateFlow<HotChallengeListUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<HotChallengeListEvents>()
    val event: SharedFlow<HotChallengeListEvents> = _event.asSharedFlow()

    fun changeFilter(filter: HotChallengeSortType) {
        _uiState.update { state ->
            state.copy(
                curFilter = filter,
                page = 0,
                hasNext = true
            )
        }

        getHotChallengeList(NEW)
    }

    fun getHotChallengeList(option: Int) {
        if (uiState.value.hasNext) {
            viewModelScope.launch {
                challengeRepository.getMoreHotChallenges(
                    uiState.value.curFilter.value,
                    uiState.value.page,
                    20
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData = it.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = if (option == NEXT_PAGE) uiState.value.uiChallengeRoom + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1
                                )
                            }

                            delay(100)

                            _event.emit(HotChallengeListEvents.ScrollToTop)
                        }

                        is BaseState.Error -> _event.emit(HotChallengeListEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _event.emit(HotChallengeListEvents.NavigateToChallengeDetail(id))
        }
    }

    fun navigateToChallengeCategory(){
        viewModelScope.launch {
            _event.emit(HotChallengeListEvents.NavigateToChallengeCategory)
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(HotChallengeListEvents.NavigateToBack)
        }
    }

}

enum class HotChallengeSortType(val value: String) {
    MOST_RECENT("MOST_RECENT"),
    MOST_HEADCOUNT("MOST_HEADCOUNT"),
    MOST_CERTIFICATION("MOST_CERTIFICATION")
}
