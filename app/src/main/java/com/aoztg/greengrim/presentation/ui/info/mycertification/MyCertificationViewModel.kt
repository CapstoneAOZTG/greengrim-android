package com.aoztg.greengrim.presentation.ui.info.mycertification

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.presentation.util.toHeaderText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import javax.inject.Inject

data class MyCertificationUiState(
    val curMonth: MonthState = MonthState.Empty,
    val curDate: DateState = DateState.Empty
)

sealed class MonthState {
    object Empty: MonthState()
    data class Changed(val stringMonth: String, val originMonth: YearMonth): MonthState()
}

sealed class DateState {
    object Empty : DateState()
    data class Changed(val stringDate: String, val originDate: LocalDate) : DateState()
}

@HiltViewModel
class MyCertificationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MyCertificationUiState())
    val uiState: StateFlow<MyCertificationUiState> = _uiState.asStateFlow()

    fun scrollMonth(date: YearMonth) {
        val stringYearMonth = date.year.toString() + "년 " + date.monthValue + "월"
        _uiState.update { state ->
            state.copy(
                curMonth = MonthState.Changed(stringYearMonth,date)
            )
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                curDate = DateState.Changed(date.toHeaderText(), date)
            )
        }
    }

}