package com.aoztg.greengrim.presentation.ui.challenge.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.SearchChallengeRequest
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.presentation.ui.challenge.mapper.toUiChallengeList
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchChallengeUiState(
    val uiChallengeRoom: List<UiChallengeRoom> = emptyList(),
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class SearchChallengeEvent{
    data class ShowSnackMessage(val msg: String) : SearchChallengeEvent()
}

@HiltViewModel
class SearchChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository
): ViewModel() {


    private val _uiState = MutableStateFlow(SearchChallengeUiState())
    val uiState : StateFlow<SearchChallengeUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<SearchChallengeEvent>()
    val event : SharedFlow<SearchChallengeEvent> = _event.asSharedFlow()

    private var categoryValue = ""

    val keyword = MutableStateFlow("")

    init {
        observeKeyword()
    }

    fun setCategoryValue( category: String ){
        categoryValue = category
    }

    private fun observeKeyword(){
        keyword.onEach {
            if(it.isNotBlank()){
                val request = if(categoryValue.isNotBlank()){
                    repository.searchChallenge(
                        categoryValue,
                        uiState.value.page,
                        10,
                        SearchChallengeRequest(it)
                    )
                } else {
                    repository.searchWholeChallenge(
                        0,
                        10,
                        SearchChallengeRequest(it)
                    )
                }

                request.let{ result ->
                    when(result){
                        is BaseState.Success -> {
                            val uiData = result.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = 1,
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _event.emit(SearchChallengeEvent.ShowSnackMessage(result.msg))
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getChallengeList(){
        if(uiState.value.hasNext){

            viewModelScope.launch {

                val request = if(categoryValue.isNotBlank()){
                    repository.searchChallenge(
                        categoryValue,
                        uiState.value.page,
                        10,
                        SearchChallengeRequest(keyword.value)
                    )
                } else {
                    repository.searchWholeChallenge(
                        uiState.value.page,
                        10,
                        SearchChallengeRequest(keyword.value)
                    )
                }
                request.let{ result ->
                    when(result){
                        is BaseState.Success -> {
                            val uiData = result.body.toUiChallengeList(::navigateToChallengeDetail)
                            _uiState.update { state ->
                                state.copy(
                                    uiChallengeRoom = uiState.value.uiChallengeRoom + uiData.result,
                                    hasNext = uiData.hasNext,
                                    page = uiData.page + 1,
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _event.emit(SearchChallengeEvent.ShowSnackMessage(result.msg))
                        }
                    }
                }
            }
        }
    }

    fun deleteKeyword(){
        keyword.value = ""
        _uiState.update { state ->
            state.copy(
                uiChallengeRoom = emptyList(),
                page = 0,
                hasNext = false,
            )
        }
    }

    private fun navigateToChallengeDetail(id: Long){
        viewModelScope.launch {

        }
    }
}