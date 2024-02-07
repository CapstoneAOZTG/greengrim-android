package com.aoztg.greengrim.presentation.ui.global.nftdetail.transaction.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiNftSellData
import com.aoztg.greengrim.presentation.ui.global.model.UiNftSellData
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

data class SellNftUiState(
    val nftInfo: UiNftSellData = UiNftSellData()
)

sealed class SellNftEvents{
    data class SellNft(val price: Double): SellNftEvents()
}

@HiltViewModel
class SellNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SellNftUiState())
    val uiState: StateFlow<SellNftUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SellNftEvents>()
    val events: SharedFlow<SellNftEvents> = _events.asSharedFlow()

    private var nftId: Long = -1L
    val price = MutableStateFlow("")
    val termState = MutableStateFlow(false)

   val isDataReady = combine(price, termState) { price, termState ->
        price.isNotBlank() && termState
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    private fun getNftInfo(){
        viewModelScope.launch {
            nftRepository.getInfoBeforeSellNft(nftId).let{
                when(it){
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                nftInfo = it.body.toUiNftSellData()
                            )
                        }
                    }
                    is BaseState.Error -> {}
                }
            }
        }
    }

    fun setNftId(id: Long){
        nftId = id
        getNftInfo()
    }

    fun sellNft(){
        viewModelScope.launch {
            _events.emit(SellNftEvents.SellNft(
                price.value.toDouble()
            ))
        }
    }

}