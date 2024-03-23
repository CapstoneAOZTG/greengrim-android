package com.aoztg.greengrim.presentation.ui.info.mykeyword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.InfoRepository
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

data class MyKeywordUiState(
    val nounKeywords: List<String> = emptyList(),
    val verbKeywords: List<String> = emptyList(),
    val styleKeywords: List<String> = emptyList(),
)

sealed class MyKeywordEvents{
    data class ShowSnackMessage(val msg: String): MyKeywordEvents()
    object NavigateToBack: MyKeywordEvents()
}

@HiltViewModel
class MyKeywordViewModel @Inject constructor(
    private val infoRepository: InfoRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MyKeywordUiState())
    val uiState: StateFlow<MyKeywordUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyKeywordEvents>()
    val events: SharedFlow<MyKeywordEvents> = _events.asSharedFlow()

    fun getMyKeywords(){
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

                    is BaseState.Error -> {
                        _events.emit(MyKeywordEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(MyKeywordEvents.NavigateToBack)
        }
    }
}