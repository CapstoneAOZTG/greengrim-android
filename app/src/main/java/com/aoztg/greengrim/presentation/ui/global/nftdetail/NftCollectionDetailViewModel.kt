package com.aoztg.greengrim.presentation.ui.global.nftdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiNftCollectionDetailInfo
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCollectionDetailInfo
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


data class NftCollectionDetailUiState(
    val uiNftCollectionDetailInfo: UiNftCollectionDetailInfo = UiNftCollectionDetailInfo()
)

sealed class NftCollectionDetailEvent{
    data class ShowSnackMessage(val msg: String) : NftCollectionDetailEvent()
    object NavigateToBack : NftCollectionDetailEvent()
    data class ShowToastMessage(val msg: String) : NftCollectionDetailEvent()
}

@HiltViewModel
class NftCollectionDetailViewModel @Inject constructor(
    private val repository: NftRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(NftCollectionDetailUiState())
    val uiState: StateFlow<NftCollectionDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<NftCollectionDetailEvent>()
    val event: SharedFlow<NftCollectionDetailEvent> = _event.asSharedFlow()

    fun getNftCollectionDetail(id: Long){
        viewModelScope.launch {
            repository.getNftCollectionDetail(id).let{
                when(it){
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiNftCollectionDetailInfo = it.body.toUiNftCollectionDetailInfo()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _event.emit(NftCollectionDetailEvent.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(NftCollectionDetailEvent.NavigateToBack)
        }
    }


}