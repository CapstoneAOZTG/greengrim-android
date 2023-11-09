package com.aoztg.greengrim.presentation.ui.info.mychallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toChallengeListData
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom
import com.google.gson.Gson
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


data class MyChallengeUiState(
    val loading: LoadingState = LoadingState.Empty,
    val challengeRoom: List<ChallengeRoom> = emptyList(),
    val sortType: ChallengeSortType = ChallengeSortType.DESC,
    val page: Int = 0,
    val hasNext: Boolean = true,
    val getChallengeRoomState: BaseState = BaseState.Empty
)

sealed class MyChallengeEvents {
    data class NavigateToChallengeDetail(val id: String) : MyChallengeEvents()
    object NavigateToCreateChallenge : MyChallengeEvents()
    object ShowBottomSheet : MyChallengeEvents()
    object ScrollToTop : MyChallengeEvents()
}

@HiltViewModel
class MyChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val _uiState = MutableStateFlow(MyChallengeUiState())
    val uiState: StateFlow<MyChallengeUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyChallengeEvents>()
    val events: SharedFlow<MyChallengeEvents> = _events.asSharedFlow()

    private var category = ""

    fun getMyChallenge(option: Int) {

        if (_uiState.value.hasNext) {
            viewModelScope.launch {

                _uiState.update {
                    it.copy(
                        loading = LoadingState.IsLoading(true),
                        page = it.page
                    )
                }

                val response = challengeRepository.getMyChallenge(
                    _uiState.value.page,
                    20,
                    _uiState.value.sortType.value
                )


                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val uiData = body.toChallengeListData(::navigateToChallengeDetail)
                        _uiState.update { state ->
                            state.copy(
                                challengeRoom = if (option == ORIGINAL) _uiState.value.challengeRoom + uiData.result else uiData.result,
                                hasNext = uiData.hasNext,
                                page = uiData.page + 1,
                                getChallengeRoomState = BaseState.Success,
                                loading = LoadingState.IsLoading(false)
                            )
                        }
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)

                    _uiState.update { state ->
                        state.copy(
                            getChallengeRoomState = BaseState.Error(error.message),
                            loading = LoadingState.IsLoading(false)
                        )
                    }
                }
            }
        }
    }

    private fun navigateToChallengeDetail(id: String) {
        viewModelScope.launch {
            _events.emit(MyChallengeEvents.NavigateToChallengeDetail(id))
        }
    }

    fun navigateToCreateChallenge() {
        viewModelScope.launch {
            _events.emit(MyChallengeEvents.NavigateToCreateChallenge)
        }
    }

    fun showBottomSheet() {
        viewModelScope.launch {
            _events.emit(MyChallengeEvents.ShowBottomSheet)
        }
    }

    fun setSortType(type: ChallengeSortType) {
        _uiState.value = _uiState.value.copy(
            hasNext = true,
            sortType = type,
            page = 0
        )
        getMyChallenge(SORT)
    }

    fun setCategory(type: String) {
        category = type
    }
}
