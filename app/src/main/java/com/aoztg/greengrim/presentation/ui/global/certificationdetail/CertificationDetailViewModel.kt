package com.aoztg.greengrim.presentation.ui.global.certificationdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiCertificationDetail
import com.aoztg.greengrim.presentation.ui.global.model.UiCertificationDetail
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

data class CertificationDetailUiState(
    val uiCertificationDetail: UiCertificationDetail = UiCertificationDetail()
)

sealed class CertificationDetailEvents {
    data class ShowToastMessage(val msg: String) : CertificationDetailEvents()
    object NavigateToBack : CertificationDetailEvents()
    object ShowSnackBar : CertificationDetailEvents()
}

@HiltViewModel
class CertificationDetailViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CertificationDetailUiState())
    val uiState: StateFlow<CertificationDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CertificationDetailEvents>()
    val events: SharedFlow<CertificationDetailEvents> = _events.asSharedFlow()

    private var certificationId = -1

    private fun getCertificationDetail() {
        viewModelScope.launch {
            certificationRepository.getCertificationDetail(certificationId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiCertificationDetail = it.body.toUiCertificationDetail()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(CertificationDetailEvents.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun verifyCertification(state: Boolean) {
        viewModelScope.launch {
            certificationRepository.verifyCertification(
                VerificationsRequest(
                    certificationId,
                    state
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiCertificationDetail = _uiState.value.uiCertificationDetail.copy(
                                    isVerified = "TRUE"
                                )
                            )
                        }
                        _events.emit(CertificationDetailEvents.ShowSnackBar)
                    }

                    is BaseState.Error -> {
                        _events.emit(CertificationDetailEvents.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }


    fun setCertificationId(id: Int) {
        certificationId = id
        getCertificationDetail()
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(CertificationDetailEvents.NavigateToBack)
        }
    }
}