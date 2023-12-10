package com.aoztg.greengrim.presentation.ui.editgrim

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.PatchGrimNameRequest
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiGrimDetail
import com.aoztg.greengrim.presentation.ui.global.model.UiGrimDetail
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

data class EditGrimUiState(
    val grimId: Int = CompleteGrim.grimId.toString().toInt(),
    val grimUrl: String = CompleteGrim.grimImgUrl.toString(),
    val grimState: GrimState = CompleteGrim.grimState,
    val uiGrimDetail: UiGrimDetail = UiGrimDetail()
)

sealed class EditGrimEvents {
    data class ShowSnackMessage(val msg: String) : EditGrimEvents()
    object GoToMainActivity: EditGrimEvents()
    data class GoToGrimDetail(val grimId: Int): EditGrimEvents()
    data class GoToCreateNft(val grimId: Int, val grimUrl: String): EditGrimEvents()
}

@HiltViewModel
class EditGrimViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditGrimUiState())
    val uiState: StateFlow<EditGrimUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<EditGrimEvents>()
    val events: SharedFlow<EditGrimEvents> = _events.asSharedFlow()

    val title = MutableStateFlow("")
    val isDataReady = combine(title) { flows ->
        flows[0].isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun patchGrimTitle() {
        viewModelScope.launch {
            nftRepository.patchGrimTitle(
                PatchGrimNameRequest(
                    id = uiState.value.grimId,
                    title = title.value
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        CompleteGrim.grimState = GrimState.GRIM_TITLED
                        getGrimDetail()
                    }

                    is BaseState.Error -> {}
                }
            }
        }
    }

    fun getGrimDetail() {
        viewModelScope.launch {
            nftRepository.getGrimDetail(uiState.value.grimId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiGrimDetail = it.body.toUiGrimDetail(),
                                grimState = GrimState.GRIM_TITLED
                            )
                        }
                    }

                    is BaseState.Error -> _events.emit(EditGrimEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    fun goToCreateNft(){
        viewModelScope.launch {
            _events.emit(EditGrimEvents.GoToCreateNft(uiState.value.grimId, uiState.value.grimUrl))
        }
    }

    fun goToGrimDetail(){
        viewModelScope.launch {
            _events.emit(EditGrimEvents.GoToGrimDetail(uiState.value.grimId))
        }
    }

}