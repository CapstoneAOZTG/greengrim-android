package com.aoztg.greengrim.presentation.ui.global.attendcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.AccusationRequest
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.repository.AttendCheckRepository
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.data.repository.MemberRepository
import com.aoztg.greengrim.presentation.customview.AccusationContentType
import com.aoztg.greengrim.presentation.customview.AccusationType
import com.aoztg.greengrim.presentation.ui.DataState
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

data class AttendCheckUiState(
    val uiCertificationDetail: UiCertificationDetail = UiCertificationDetail(),
    val dataState: DataState = DataState.BEFORE
)

sealed class AttendCheckEvents {
    object ShowVerifySnackBar : AttendCheckEvents()
    data class ShowToastMessage(val msg: String) : AttendCheckEvents()
    object NavigateToBack : AttendCheckEvents()
    object ShowLoading : AttendCheckEvents()
    object DismissLoading : AttendCheckEvents()
    object ShowPopUp : AttendCheckEvents()
    data class ShowSnackMessage(val msg: String) : AttendCheckEvents()
}

@HiltViewModel
class AttendCheckViewModel @Inject constructor(
    private val attendCheckRepository: AttendCheckRepository,
    private val certificationRepository: CertificationRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    companion object {
        const val CERTIFICATION_002 = "CERTIFICATION_002"
    }

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
                                dataState = DataState.HAVE_DATA,
                                uiCertificationDetail = it.body.toUiCertificationDetail()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        if (it.code == CERTIFICATION_002) {
                            _uiState.update { state ->
                                state.copy(
                                    dataState = DataState.NO_DATA
                                )
                            }
                        } else {
                            _events.emit(AttendCheckEvents.ShowSnackMessage(it.msg))
                        }

                    }
                }
            }
        }
    }

    fun showPopUp(){
        viewModelScope.launch {
            _events.emit(AttendCheckEvents.ShowPopUp)
        }
    }

    fun verifyCertification(state: Boolean) {
        viewModelScope.launch {

            attendCheckRepository.verifyCertification(
                VerificationsRequest(
                    _uiState.value.uiCertificationDetail.certificationId,
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
                        _events.emit(AttendCheckEvents.ShowVerifySnackBar)
                    }

                    is BaseState.Error -> {
                        _events.emit(AttendCheckEvents.ShowSnackMessage(it.msg))
                    }
                }
            }

        }
    }

    fun blockCertification() {
        viewModelScope.launch {
            certificationRepository.hideCertification(uiState.value.uiCertificationDetail.certificationId)
                .let {
                    when (it) {
                        is BaseState.Success -> _events.emit(AttendCheckEvents.ShowToastMessage("인증 차단 성공"))
                        is BaseState.Error -> _events.emit(AttendCheckEvents.ShowSnackMessage(it.msg))
                    }
                }
        }
    }

    fun accusationCertification(type: AccusationContentType, content: String) {
        viewModelScope.launch {
            memberRepository.accusation(
                AccusationType.CERTIFICATION.text, AccusationRequest(
                    uiState.value.uiCertificationDetail.certificationId, type.text, content
                )
            ).let {
                when (it) {
                    is BaseState.Success -> _events.emit(AttendCheckEvents.ShowToastMessage("Nft 신고 성공"))
                    is BaseState.Error -> _events.emit(AttendCheckEvents.ShowSnackMessage(it.msg))
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