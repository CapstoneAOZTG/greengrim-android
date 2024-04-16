package com.aoztg.greengrim.presentation.ui.mypage.myprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.challenge.list.SortType
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toUiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
import com.aoztg.greengrim.presentation.ui.mypage.mychallenge.MyChallengeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyProfileUiState(
    val page: Int = 0,
    val hasNext: Boolean = true,
    val uiChallengeRoom: List<UiChallengeRoom> = emptyList(),
    val sortType: SortType = SortType.DESC,
    val curFilter: CurProfileFilter = CurProfileFilter.CHALLENGE
)

sealed class MyProfileEvent{
    data class NavigateToChallengeDetail(val id: Long) : MyProfileEvent()
    data class ShowToastMessage(val msg: String) : MyProfileEvent()
    data class ShowSnackMessage(val msg: String) : MyProfileEvent()
    object ShowChallengeFilterBottomSheet : MyProfileEvent()
}

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val challengeRepository : ChallengeRepository
): ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(MyProfileUiState())
    val uiState : StateFlow<MyProfileUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyProfileEvent>()
    val events: SharedFlow<MyProfileEvent> = _events.asSharedFlow()

    fun setChallengeSortType(type: SortType){
        _uiState.value = uiState.value.copy(
            hasNext = true,
            sortType = type,
            page = 0
        )

        getMyChallenge(SORT)
    }

    fun getMyChallenge(option: Int) {

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                challengeRepository.getMyChallenge(
                    _uiState.value.page,
                    20,
                    _uiState.value.sortType.value
                ).let {
                    when (it) {
                        is BaseState.Success -> {
                            val uiData = it.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = if (option == MyChallengeViewModel.ORIGINAL) _uiState.value.uiChallengeRoom + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _events.emit(MyProfileEvent.ShowSnackMessage(it.msg))
                        }
                    }
                }
            }
        }
    }

    fun showChallengeFilterBottomSheet() {
        viewModelScope.launch {
            _events.emit(MyProfileEvent.ShowChallengeFilterBottomSheet)
        }
    }

    private fun navigateToChallengeDetail(id: Long) {
        viewModelScope.launch {
            _events.emit(MyProfileEvent.NavigateToChallengeDetail(id))
        }
    }

}

enum class CurProfileFilter(){
    CHALLENGE,
    CERTIFICATION,
    NFT
}