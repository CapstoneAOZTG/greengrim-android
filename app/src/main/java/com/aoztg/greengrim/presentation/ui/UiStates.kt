package com.aoztg.greengrim.presentation.ui

import java.time.LocalDate
import java.time.YearMonth


sealed class LoadingState {
    object Empty : LoadingState()
    data class IsLoading(val state: Boolean) : LoadingState()
}

sealed class BaseUiState {
    object Empty : BaseUiState()
    object Success : BaseUiState()
    object Failure : BaseUiState()
    data class Error(val msg: String) : BaseUiState()
}

sealed class MonthState {
    object Empty : MonthState()
    data class Changed(val stringMonth: String, val originMonth: YearMonth) : MonthState()
}

sealed class DateState {
    object Empty : DateState()
    data class Changed(val stringDate: String, val originDate: LocalDate) : DateState()
}