package com.aoztg.greengrim.presentation.ui.global.attendcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.repository.AttendCheckRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.global.model.CertificationDetail
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

data class AttendCheckUiState(
    val certificationDetail: CertificationDetail = CertificationDetail()
)

sealed class AttendCheckEvents {
    object ShowSnackBar : AttendCheckEvents()
    data class ShowToastMessage(val msg: String) : AttendCheckEvents()
    object NavigateToBack : AttendCheckEvents()
}


@HiltViewModel
class AttendCheckViewModel @Inject constructor(
    private val attendCheckRepository: AttendCheckRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendCheckUiState())
    val uiState: StateFlow<AttendCheckUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AttendCheckEvents>()
    val events: SharedFlow<AttendCheckEvents> = _events.asSharedFlow()

    fun getCertificationForVerify() {
        viewModelScope.launch {

            attendCheckRepository.getCertificationForVerify().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                certificationDetail = it.body.toCertificationDetail()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(AttendCheckEvents.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun verifyCertification(state: Boolean) {
        viewModelScope.launch {

            attendCheckRepository.verifyCertification(
                VerificationsRequest(
                    _uiState.value.certificationDetail.certificationId,
                    state
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                certificationDetail = _uiState.value.certificationDetail.copy(
                                    isVerified = "TRUE"
                                )
                            )
                        }
                        _events.emit(AttendCheckEvents.ShowSnackBar)
                    }

                    is BaseState.Error -> {
                        _events.emit(AttendCheckEvents.ShowToastMessage(it.msg))
                    }
                }
            }

        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(AttendCheckEvents.NavigateToBack)
        }
    }
}