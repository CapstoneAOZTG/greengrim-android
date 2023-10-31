package com.aoztg.greengrim.presentation.ui.challenge.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class CreateChallengeDetailUiState(
    val certificateProgressState: ProgressState = ProgressState.Empty,
    val ticketProgressState: ProgressState = ProgressState.Empty,
    val minCertificateProgressState: ProgressState = ProgressState.Empty
)

sealed class ProgressState {
    object Empty : ProgressState()
    data class Changed(val text: String) : ProgressState()
}

@HiltViewModel
class CreateChallengeDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CreateChallengeDetailUiState())
    val uiState: StateFlow<CreateChallengeDetailUiState> = _uiState.asStateFlow()

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    private var imageUrl = ""

    val certificateProgress = MutableStateFlow(0)
    val ticketProgress = MutableStateFlow(0)
    val minCertificateProgress = MutableStateFlow(0)

    init {
        observeSeekBar()
    }

    private fun observeSeekBar() {
        certificateProgress.onEach { progress ->
            _uiState.update { state ->
                state.copy(
                    certificateProgressState = ProgressState.Changed(
                        (10 + (progress * 5)).toString() + "회"
                    )
                )
            }
        }.launchIn(viewModelScope)

        ticketProgress.onEach { progress ->
            _uiState.update { state ->
                state.copy(
                    ticketProgressState = ProgressState.Changed(
                        (20 + (progress * 20)).toString() + "개"
                    )
                )
            }
        }.launchIn(viewModelScope)

        minCertificateProgress.onEach { progress ->
            _uiState.update { state ->
                state.copy(
                    minCertificateProgressState = ProgressState.Changed(
                        "주 " + (2 + progress).toString() + "회"
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setImageUrl(url: String) {
        imageUrl = url
    }
}