package com.aoztg.greengrim.presentation.ui.chat.createcertification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.data.repository.ImageRepository
import com.aoztg.greengrim.presentation.ui.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
import okhttp3.MultipartBody
import javax.inject.Inject


data class CreateCertificationUiState(
    val nextBtnState: BaseUiState = BaseUiState.Empty,
    val challengeName: String = "",
    val certificationCount: Int = 0,
    val category: String = "",
    val ticketCount: String = ""
)

sealed class CreateCertificationEvents {
    object NavigateToBack : CreateCertificationEvents()
    data class SendCertificationMessage(
        val message: String,
        val certId: Long,
        var certImg: String
    ) : CreateCertificationEvents()

    data class ShowToastMessage(val msg: String) : CreateCertificationEvents()
    data class ShowSnackMessage(val msg: String) : CreateCertificationEvents()
    object ShowLoading : CreateCertificationEvents()
    object DismissLoading : CreateCertificationEvents()
}

@HiltViewModel
class CreateCertificationViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCertificationUiState())
    val uiState: StateFlow<CreateCertificationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateCertificationEvents>()
    val events: SharedFlow<CreateCertificationEvents> = _events.asSharedFlow()

    val description = MutableStateFlow("")
    val isImageSet = MutableStateFlow(false)
    private var challengeId = -1L

    val isDataReady = combine(description, isImageSet) { description, isImageSet ->
        description.isNotBlank() && isImageSet
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    private var imageStoreJob: Job? = null
    private var imageUrl = ""


    fun setInfo(
        id: Long,
        challengeName: String,
        certificationCount: Int,
        category: String,
        ticketCount: String
    ) {
        challengeId = id
        _uiState.update { state ->
            state.copy(
                challengeName = challengeName,
                certificationCount = certificationCount,
                category = category,
                ticketCount = ticketCount
            )
        }
    }


    fun setImageFile(
        file: MultipartBody.Part
    ) {
        isImageSet.value = true
        imageStoreJob = viewModelScope.launch {
            imageRepository.imageToUrl(file).let {
                when (it) {
                    is BaseState.Success -> imageUrl = it.body.imgUrl

                    is BaseState.Error -> _events.emit(CreateCertificationEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    fun createCertification() {

        viewModelScope.launch {
            _events.emit(CreateCertificationEvents.ShowLoading)
            imageStoreJob?.join()

            certificationRepository.createCertification(
                CreateCertificationRequest(
                    challengeId = challengeId,
                    imgUrl = imageUrl,
                    description = description.value,
                    round = _uiState.value.certificationCount
                )
            ).let {
                _events.emit(CreateCertificationEvents.DismissLoading)

                when (it) {

                    is BaseState.Success -> {
                        _events.emit(
                            CreateCertificationEvents.SendCertificationMessage(
                                message = "[${_uiState.value.certificationCount}회차] ${it.body.date}\n${description.value}",
                                certId = it.body.certId,
                                certImg = it.body.certImg
                            )
                        )
                        _events.emit(CreateCertificationEvents.ShowToastMessage("인증 업로드 성공"))
                        _events.emit(CreateCertificationEvents.NavigateToBack)
                    }

                    is BaseState.Error -> {
                        _events.emit(CreateCertificationEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }


    fun navigateBack() {
        viewModelScope.launch {
            _events.emit(CreateCertificationEvents.NavigateToBack)
        }
    }

}