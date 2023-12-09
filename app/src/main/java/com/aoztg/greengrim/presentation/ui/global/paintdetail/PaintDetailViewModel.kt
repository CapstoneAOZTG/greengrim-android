package com.aoztg.greengrim.presentation.ui.global.paintdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiGrimDetail
import com.aoztg.greengrim.presentation.ui.global.model.UiGrimDetail
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



data class PaintDetailUiState(
    val uiDetailData: UiGrimDetail = UiGrimDetail()
)

sealed class PaintDetailEvents{
    data class ShowSnackMessage(val msg: String): PaintDetailEvents()
    object NavigateToBack: PaintDetailEvents()
}

@HiltViewModel
class PaintDetailViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PaintDetailUiState())
    val uiState:StateFlow<PaintDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<PaintDetailEvents>()
    val events: SharedFlow<PaintDetailEvents> = _events.asSharedFlow()

    private fun getPaintDetail(id: Int){
        viewModelScope.launch {
            nftRepository.getGrimDetail(id).let{
                when(it){
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiDetailData = it.body.toUiGrimDetail()
                            )
                        }
                    }
                    is BaseState.Error -> _events.emit(PaintDetailEvents.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    fun setId(id: Int){
        getPaintDetail(id)
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(PaintDetailEvents.NavigateToBack)
        }
    }
}