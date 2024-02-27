package com.aoztg.greengrim.presentation.ui.global.nftdetail.transaction.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.PurchaseNftRequest
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiNftPurchaseData
import com.aoztg.greengrim.presentation.ui.global.model.UiNftPurchaseData
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


data class PurchaseNftUiState(
    val nftInfo: UiNftPurchaseData = UiNftPurchaseData()
)

sealed class PurchaseNftEvents{
    data class ShowSnackMessage(val msg: String): PurchaseNftEvents()
    data class PurchaseNft(val marketId: Long): PurchaseNftEvents()
}

@HiltViewModel
class PurchaseNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {

    private val _uiSate = MutableStateFlow(PurchaseNftUiState())
    val uiState: StateFlow<PurchaseNftUiState> = _uiSate.asStateFlow()

    private val _events = MutableSharedFlow<PurchaseNftEvents>()
    val events: SharedFlow<PurchaseNftEvents> = _events.asSharedFlow()

    val termState = MutableStateFlow(false)

    private var nftId: Long = -1L

    private fun getNftInfo(){
        viewModelScope.launch {
            nftRepository.getInfoBeforePurchaseNft(nftId).let{
                when(it){
                    is BaseState.Success -> {
                        _uiSate.update { state ->
                            state.copy(
                                nftInfo = it.body.toUiNftPurchaseData()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(PurchaseNftEvents.ShowSnackMessage(it.msg))
                    }
                }

            }
        }
    }

    fun setNftId(id: Long){
        nftId = id
        getNftInfo()
    }

    fun purchaseNft(){
        viewModelScope.launch {
            _events.emit(PurchaseNftEvents.PurchaseNft(uiState.value.nftInfo.marketId))
        }
    }

}