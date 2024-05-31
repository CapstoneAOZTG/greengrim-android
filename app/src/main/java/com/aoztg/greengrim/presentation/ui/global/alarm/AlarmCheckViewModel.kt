package com.aoztg.greengrim.presentation.ui.global.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiAlarmData
import com.aoztg.greengrim.presentation.ui.global.model.UiAlarmData
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

data class AlarmCheckUiState(
    val uiAlarmData: List<UiAlarmData> = emptyList(),
    val page: Int = 0,
    val hasNext: Boolean = true,
)

sealed class AlarmCheckEvent {

}

@HiltViewModel
class AlarmCheckViewModel @Inject constructor(
    private val repository: MemberRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmCheckUiState())
    val uiState: StateFlow<AlarmCheckUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<AlarmCheckEvent>()
    val event: SharedFlow<AlarmCheckEvent> = _event.asSharedFlow()

    fun getAlarmList() {
        if (uiState.value.hasNext) {
            viewModelScope.launch {
                repository.getAlarmCheck(uiState.value.page, 20).let {
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    uiAlarmData = it.body.result.map { data ->
                                        data.toUiAlarmData()
                                    },
                                    hasNext = it.body.hasNext,
                                    page = it.body.page + 1
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

}