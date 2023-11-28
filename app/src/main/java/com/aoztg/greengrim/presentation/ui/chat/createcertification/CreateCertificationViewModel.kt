package com.aoztg.greengrim.presentation.ui.chat.createcertification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.model.response.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.presentation.ui.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CreateCertificationUiState(
    val nextBtnState: BaseUiState = BaseUiState.Empty,
    val certificationDefaultData: CertificationDefaultDataResponse = CertificationDefaultDataResponse()
)

sealed class CreateCertificationEvents {
    object NavigateToBack : CreateCertificationEvents()
    data class SendCertificationMessage(
        val message: String,
        val certId: Int,
        var certImg: String
    ) : CreateCertificationEvents()

    data class ShowToastMessage(val msg: String) : CreateCertificationEvents()
}

@HiltViewModel
class CreateCertificationViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCertificationUiState())
    val uiState: StateFlow<CreateCertificationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateCertificationEvents>()
    val events: SharedFlow<CreateCertificationEvents> = _events.asSharedFlow()

    val description = MutableStateFlow("")
    val certificationImgUrl = MutableStateFlow("")
    private var challengeId = -1

    val isDataReady = combine(description, certificationImgUrl) { description, imgUrl ->
        description.isNotBlank() && imgUrl.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun getCertificationDefaultData() {
        viewModelScope.launch {

            certificationRepository.getCertificationDefaultData(challengeId)
                .let {
                    when (it) {
                        is BaseState.Success -> {
                            _uiState.update { state ->
                                state.copy(
                                    certificationDefaultData = it.body
                                )
                            }
                        }

                        is BaseState.Error -> {
                            _events.emit(CreateCertificationEvents.ShowToastMessage(it.msg))
                        }
                    }
                }

        }
    }

    fun createCertification() {

        viewModelScope.launch {
            certificationRepository.createCertification(
                CreateCertificationRequest(
                    challengeId = challengeId,
                    imgUrl = certificationImgUrl.value,
                    description = description.value,
                    round = _uiState.value.certificationDefaultData.round
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        _events.emit(
                            CreateCertificationEvents.SendCertificationMessage(
                                message = "[${_uiState.value.certificationDefaultData.round}회차] ${it.body.date}\n${description.value}",
                                certId = it.body.certId,
                                certImg = it.body.certImg
                            )
                        )
                        _events.emit(CreateCertificationEvents.NavigateToBack)
                    }

                    is BaseState.Error -> {
                        _events.emit(CreateCertificationEvents.ShowToastMessage(it.msg))
                    }
                }
            }
        }
    }

    fun setImgUrl(url: String) {
        certificationImgUrl.value = url
    }

    fun setIds(challengeIdData: Int) {
        challengeId = challengeIdData
    }

    fun navigateBack() {
        viewModelScope.launch {
            _events.emit(CreateCertificationEvents.NavigateToBack)
        }
    }

}