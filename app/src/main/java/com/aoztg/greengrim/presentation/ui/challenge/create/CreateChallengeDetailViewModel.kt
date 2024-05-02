package com.aoztg.greengrim.presentation.ui.challenge.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.repository.ChallengeRepository
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject


data class CreateChallengeDetailUiState(
    val titleState: BaseUiState = BaseUiState.Empty,
    val descriptionState: BaseUiState = BaseUiState.Empty,
    val certificateProgressState: ProgressState = ProgressState.Empty,
    val ticketProgressState: ProgressState = ProgressState.Empty,
    val minCertificateProgressState: ProgressState = ProgressState.Empty,
)

sealed class ProgressState {
    object Empty : ProgressState()
    data class Changed(val text: String) : ProgressState()
}

sealed class CreateChallengeDetailEvents {
    object NavigateToBack : CreateChallengeDetailEvents()
    data class NavigateToChatList(
        val chatId: Long,
        val challengeId: Long,
        val title: String,
        val titleImg: String
    ) : CreateChallengeDetailEvents()

    data class ShowToastMessage(val msg: String) : CreateChallengeDetailEvents()
    data class ShowSnackMessage(val msg: String) : CreateChallengeDetailEvents()
    object ShowLoading : CreateChallengeDetailEvents()
    object DismissLoading : CreateChallengeDetailEvents()
}

@HiltViewModel
class CreateChallengeDetailViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateChallengeDetailUiState())
    val uiState: StateFlow<CreateChallengeDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateChallengeDetailEvents>()
    val events: SharedFlow<CreateChallengeDetailEvents> = _events.asSharedFlow()

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val category = MutableStateFlow("")
    val certificateProgress = MutableStateFlow(0)
    val ticketProgress = MutableStateFlow(0)
    val minCertificateProgress = MutableStateFlow(0)
    private val imageSet = MutableStateFlow(false)
    private var imgFile: MultipartBody.Part? = null

    private var goalCount = 0
    private var ticketTotalCount = 0
    private var weekMinCount = 0

    private var imageStoreJob: Job? = null
    private var imageUrl = ""

    val isDataReady = combine(
        title,
        description,
        imageSet,
        category
    ) { title, description, imageSet, category ->
        title.length >= 2 && description.length >= 2
                && imageSet && category.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setImageFile(
        file: MultipartBody.Part
    ) {
        imageSet.value = true
        imageStoreJob = viewModelScope.launch {

            imageRepository.imageToUrl(file).let {
                when (it) {
                    is BaseState.Success -> imageUrl = it.body.imgUrl
                    is BaseState.Error -> _events.emit(
                        CreateChallengeDetailEvents.ShowSnackMessage(
                            it.msg
                        )
                    )
                }
            }
        }
    }

    init {
        observeTitle()
        observeDescription()
        observeSeekBar()
    }

    private fun observeTitle() {
        title.onEach {
            if (it.isNotBlank()) {
                if (it.length < 2) {
                    _uiState.update { state ->
                        state.copy(
                            titleState = BaseUiState.Error("2글자 이상 입력해주세요")
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            titleState = BaseUiState.Success
                        )
                    }
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        titleState = BaseUiState.Empty
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeDescription() {
        description.onEach {
            if (it.isNotBlank()) {
                if (it.length < 2) {
                    _uiState.update { state ->
                        state.copy(
                            descriptionState = BaseUiState.Error("2글자 이상 입력해주세요")
                        )
                    }
                } else {
                    _uiState.update { state ->
                        state.copy(
                            descriptionState = BaseUiState.Success
                        )
                    }
                }
            } else {
                _uiState.update { state ->
                    state.copy(
                        descriptionState = BaseUiState.Empty
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeSeekBar() {
        certificateProgress.onEach { progress ->
            goalCount = 10 + progress * 5
            _uiState.update { state ->
                state.copy(
                    certificateProgressState = ProgressState.Changed(
                        goalCount.toString() + "회"
                    ),
                )
            }
        }.launchIn(viewModelScope)

        ticketProgress.onEach { progress ->
            ticketTotalCount = 20 + progress * 20
            _uiState.update { state ->
                state.copy(
                    ticketProgressState = ProgressState.Changed(
                        ticketTotalCount.toString() + "개"
                    ),
                )
            }
        }.launchIn(viewModelScope)

        minCertificateProgress.onEach { progress ->
            weekMinCount = 2 + progress
            _uiState.update { state ->
                state.copy(
                    minCertificateProgressState = ProgressState.Changed(
                        "주 " + weekMinCount.toString() + "회"
                    ),
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setCategory(data: String) {
        category.value = data
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(CreateChallengeDetailEvents.NavigateToBack)
        }
    }

    fun createChallenge() {
        viewModelScope.launch {

            _events.emit(CreateChallengeDetailEvents.ShowLoading)

            imageStoreJob?.join()

            challengeRepository.createChallenge(
                CreateChallengeRequest(
                    category = category.value,
                    title = title.value,
                    description = description.value,
                    imgUrl = imageUrl,
                    goalCount = goalCount,
                    ticketTotalCount = ticketTotalCount,
                    weekMinCount = weekMinCount,
                )
            ).let {
                _events.emit(CreateChallengeDetailEvents.DismissLoading)

                when (it) {
                    is BaseState.Success -> {
                        _events.emit(CreateChallengeDetailEvents.ShowToastMessage("챌린지가 생성되었습니다!"))
                        _events.emit(
                            CreateChallengeDetailEvents.NavigateToChatList(
                                it.body.chatroomId,
                                it.body.challengeId,
                                it.body.title,
                                it.body.imgUrl
                            )
                        )
                    }

                    is BaseState.Error -> {
                        _events.emit(CreateChallengeDetailEvents.ShowSnackMessage(it.msg))
                    }
                }

            }
        }

    }


}