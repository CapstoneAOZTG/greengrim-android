package com.aoztg.greengrim.presentation.ui.market.create.createpaint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.DrawGrimRequest
import com.aoztg.greengrim.data.repository.InfoRepository
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.market.DrawingState
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


data class CreatePaintUiState(
    val nounKeywords: List<String> = emptyList(),
    val verbKeywords: List<String> = emptyList(),
    val styleKeywords: List<String> = emptyList(),
)

sealed class CreatePaintEvents {
    object ShowConfirmModal : CreatePaintEvents()
    data class ShowSnackMessage(val msg: String) : CreatePaintEvents()
    data class ShowToastMessage(val msg: String) : CreatePaintEvents()
    object NavigateToWaitDrawing : CreatePaintEvents()
}

@HiltViewModel
class CreatePaintViewModel @Inject constructor(
    private val nftRepository: NftRepository,
    private val infoRepository: InfoRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(CreatePaintUiState())
    val uiState: StateFlow<CreatePaintUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreatePaintEvents>()
    val events: SharedFlow<CreatePaintEvents> = _events.asSharedFlow()

    private val selectNoun = MutableStateFlow("")
    private val selectVerb = MutableStateFlow("")
    private val selectStyle = MutableStateFlow("")

    val isDataReady = combine(
        selectNoun, selectVerb, selectStyle
    ) { noun, verb, style ->
        noun.isNotBlank() && verb.isNotBlank() && style.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun getKeywords() {
        viewModelScope.launch {
            infoRepository.getMyKeywords().let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                nounKeywords = it.body.noun,
                                verbKeywords = it.body.verb,
                                styleKeywords = it.body.style
                            )
                        }
                    }

                    is BaseState.Error -> {}
                }
            }
        }
    }

    fun selectKeyword(type: KeywordType, keyword: String) {
        when (type) {
            KeywordType.NOUN -> selectNoun.value = keyword
            KeywordType.VERB -> selectVerb.value = keyword
            KeywordType.STYLE -> selectStyle.value = keyword
        }
    }

    fun showConfirmModal() {
        viewModelScope.launch {
            _events.emit(CreatePaintEvents.ShowConfirmModal)
        }
    }

    fun startDrawGrim() {
        viewModelScope.launch {
            nftRepository.drawGrim(
                DrawGrimRequest(
                    selectNoun.value,
                    selectVerb.value,
                    selectStyle.value
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        DrawingState.DRAWING
                        _events.emit(CreatePaintEvents.NavigateToWaitDrawing)
                    }

                    is BaseState.Error -> _events.emit(CreatePaintEvents.ShowSnackMessage(it.msg))
                }
            }
        }

    }

}

enum class KeywordType {
    NOUN,
    VERB,
    STYLE
}