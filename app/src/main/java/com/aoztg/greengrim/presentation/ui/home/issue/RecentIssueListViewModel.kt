package com.aoztg.greengrim.presentation.ui.home.issue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.ui.home.mapper.toUiRecentIssueItem
import com.aoztg.greengrim.presentation.ui.home.model.UiRecentIssueItem
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


data class RecentIssueUiState(
    val page: Int = 0,
    val hasNext : Boolean = true,
    val uiIssueList: List<UiRecentIssueItem> = emptyList()
)

sealed class RecentIssueEvent{
    data class NavigateToIssueDetail(val id: Long) : RecentIssueEvent()
    object NavigateToBack: RecentIssueEvent()
}

@HiltViewModel
class RecentIssueListViewModel @Inject constructor(
    private val repository: MemberRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(RecentIssueUiState())
    val uiState: StateFlow<RecentIssueUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<RecentIssueEvent>()
    val event: SharedFlow<RecentIssueEvent> = _event.asSharedFlow()

    fun getIssueList(){
        if(uiState.value.hasNext){
            viewModelScope.launch {
                repository.getIssueList(
                    uiState.value.page,
                    20
                ).let{
                    when(it){
                        is BaseState.Success ->{
                            _uiState.update { state ->
                                state.copy(
                                    uiIssueList = it.body.result.map { data ->
                                        data.toUiRecentIssueItem(::navigateToIssueDetail)
                                    },
                                    page = it.body.page + 1,
                                    hasNext = it.body.hasNext
                                )
                            }
                        }

                        is BaseState.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun navigateToIssueDetail(id: Long){
        viewModelScope.launch {
            _event.emit(RecentIssueEvent.NavigateToIssueDetail(id))
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(RecentIssueEvent.NavigateToBack)
        }
    }


}