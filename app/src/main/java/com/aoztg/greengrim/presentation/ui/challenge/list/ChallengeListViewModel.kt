package com.aoztg.greengrim.presentation.ui.challenge.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.BaseUiState
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toChallengeListData
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom
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

data class ChallengeListUiState(
    val loading: LoadingState = LoadingState.Empty,
    val challengeRoom: List<ChallengeRoom> = emptyList(),
    val sortType: ChallengeSortType = ChallengeSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
    val getChallengeRoomState: BaseUiState = BaseUiState.Empty
)

sealed class ChallengeListEvents {
    data class NavigateToChallengeDetail(val id: Int) : ChallengeListEvents()
    object NavigateToCreateChallenge : ChallengeListEvents()
    object ShowBottomSheet : ChallengeListEvents()
    object ScrollToTop : ChallengeListEvents()
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

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                _uiState.update {
                    it.copy(
                        loading = LoadingState.IsLoading(true),
                        page = it.page
                    )
                }

                challengeRepository.getChallengeList(
                    category,
                    _uiState.value.page,
                    20,
                    _uiState.value.sortType.value
                ).let{
                    when(it){
                        is BaseState.Success -> {
                            val uiData = it.body.toChallengeListData(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    challengeRoom = if (option == ORIGINAL) _uiState.value.challengeRoom + uiData.result else uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                    getChallengeRoomState = BaseUiState.Success,
                                    loading = LoadingState.IsLoading(false)
                                )
                            }
                        }
                        is BaseState.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    getChallengeRoomState = BaseUiState.Error(it.msg),
                                    loading = LoadingState.IsLoading(false)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToChallengeDetail(id: Int) {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToChallengeDetail(id))
        }
    }

    fun navigateToCreateChallenge() {
        viewModelScope.launch {
            _events.emit(ChallengeListEvents.NavigateToCreateChallenge)
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
            sortType = type,
            page = 0
        )
        getChallengeList(SORT)
    }

    fun setCategory(type: String) {
        category = type
    }
}

enum class ChallengeSortType(val text: String, val value: String) {
    DESC("최신순", "DESC"),
    ASC("오래된 순", "ASC"),
    GREATEST("인원 많은 순", "GREATEST"),
    LEAST("인원 적은 순", "LEAST")
}