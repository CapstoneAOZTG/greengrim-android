package com.aoztg.greengrim.presentation.ui.market.create.createnft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.market.adapter.ChooseGrimAdapter
import com.aoztg.greengrim.presentation.ui.market.mapper.toUiGrimForNft
import com.aoztg.greengrim.presentation.ui.market.model.UiGrimForNft
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

data class ChooseGrimUiState(
    val page: Int = 0,
    val hasNext: Boolean = true,
    val grimList: List<UiGrimForNft> = emptyList()
)

sealed class ChooseGrimEvent{
    object NavigateToBack: ChooseGrimEvent()
    object NavigateToCreatePaint: ChooseGrimEvent()
    data class NavigateToCreateNft(val grimId: Int): ChooseGrimEvent()
    data class ShowSnackMessage(val msg: String): ChooseGrimEvent()
}

@HiltViewModel
class ChooseGrimViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseGrimUiState())
    val uiState: StateFlow<ChooseGrimUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<ChooseGrimEvent>()
    val events: SharedFlow<ChooseGrimEvent> = _events.asSharedFlow()

    fun getMyGrimForNft() {
        viewModelScope.launch {
            nftRepository.getMyGrimForNft(
                uiState.value.page,
                20
            ).let{
                when(it){
                    is BaseState.Success -> {
                        val uiData = it.body.result.map{ data -> data.toUiGrimForNft(::navigateToCreateNft)}
                        _uiState.update { state ->
                            state.copy(
                                page = it.body.page + 1,
                                hasNext = it.body.hasNext,
                                grimList = uiData
                            )
                        }
                    }
                    is BaseState.Error -> _events.emit(ChooseGrimEvent.ShowSnackMessage(it.msg))
                }
            }
        }
    }

    private fun navigateToCreateNft(grimId: Int){
        viewModelScope.launch {
            _events.emit(ChooseGrimEvent.NavigateToCreateNft(grimId))
        }
    }

    private fun navigateToCreatePaint(){
        viewModelScope.launch {
            _events.emit(ChooseGrimEvent.NavigateToCreatePaint)
        }
    }

    private fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(ChooseGrimEvent.NavigateToBack)
        }
    }

}