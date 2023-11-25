package com.aoztg.greengrim.presentation.ui.global.attendcheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.repository.AttendCheckRepository
import com.aoztg.greengrim.presentation.ui.global.certificationdetail.CertificationDetailEvents
import com.aoztg.greengrim.presentation.ui.global.mapper.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.global.model.CertificationDetail
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

data class AttendCheckUiState(
    val certificationDetail: CertificationDetail = CertificationDetail()
)

sealed class AttendCheckEvents{
    data class ShowToastMessage(val msg: String): AttendCheckEvents()
    object NavigateToBack: AttendCheckEvents()
}


@HiltViewModel
class AttendCheckViewModel @Inject constructor(
    private val attendCheckRepository: AttendCheckRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AttendCheckUiState())
    val uiState: StateFlow<AttendCheckUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AttendCheckEvents>()
    val events: SharedFlow<AttendCheckEvents> = _events.asSharedFlow()

    fun getCertificationForVerify(){
        viewModelScope.launch {
            val response = attendCheckRepository.getCertificationForVerify()

            if(response.isSuccessful){
                response.body()?.let{ body ->
                    _uiState.update { state ->
                        state.copy(
                            certificationDetail = body.toCertificationDetail()
                        )
                    }
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(AttendCheckEvents.ShowToastMessage(error.message))
            }
        }
    }

    fun verifyCertification(state: Boolean){
        viewModelScope.launch {
            val response = attendCheckRepository.verifyCertification(
                VerificationsRequest(
                    _uiState.value.certificationDetail.certificationId,
                    state
                )
            )

            if(response.isSuccessful){
                _uiState.update { state ->
                    state.copy(
                        certificationDetail = _uiState.value.certificationDetail.copy(
                            isVerified = "TRUE"
                        )
                    )
                }
            } else {
                val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _events.emit(AttendCheckEvents.ShowToastMessage(error.message))
            }
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(AttendCheckEvents.NavigateToBack)
        }
    }
}