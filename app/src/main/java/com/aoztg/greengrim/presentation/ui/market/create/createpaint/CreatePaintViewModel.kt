package com.aoztg.greengrim.presentation.ui.market.create.createpaint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

sealed class CreatePaintEvents{
    object ShowConfirmModal: CreatePaintEvents()
}

@HiltViewModel
class CreatePaintViewModel @Inject constructor(): ViewModel() {


    private val _uiState = MutableStateFlow(CreatePaintUiState())
    val uiState: StateFlow<CreatePaintUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreatePaintEvents>()
    val events: SharedFlow<CreatePaintEvents> = _events.asSharedFlow()

    private val selectNoun = MutableStateFlow("")
    private val selectVerb = MutableStateFlow("")
    private val selectStyle = MutableStateFlow("")

    val isDataReady = combine(
        selectNoun, selectVerb, selectStyle
    ) { noun, verb, style->
        noun.isNotBlank() && verb.isNotBlank() && style.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun getKeywords(){
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    nounKeywords = listOf("기본키워드", "고양이","키워드","키워드","키워드","키워드","키워드","키워드","키워드","키워드","키워드"),
                    verbKeywords = listOf("기본키워드", "키워드","키워드","키워드","키워드","키워드","키워드","키워드"),
                    styleKeywords = listOf("3d-model", "fantasy", "line art", "pixel art")
                )
            }

        }
    }

    fun selectKeyword(type: KeywordType, keyword: String){
        when(type){
            KeywordType.NOUN -> selectNoun.value = keyword
            KeywordType.VERB -> selectVerb.value = keyword
            KeywordType.STYLE -> selectStyle.value = keyword
        }
    }

    fun showConfirmModal(){
        viewModelScope.launch {
            _events.emit(CreatePaintEvents.ShowConfirmModal)
        }
    }

}

enum class KeywordType{
    NOUN,
    VERB,
    STYLE
}