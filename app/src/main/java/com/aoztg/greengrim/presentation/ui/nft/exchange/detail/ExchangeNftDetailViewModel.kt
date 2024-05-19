package com.aoztg.greengrim.presentation.ui.nft.exchange.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.DataState
import com.aoztg.greengrim.presentation.ui.nft.mapper.toUiNftSimpleInfo
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftSimpleInfo
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


data class ExchangeNftDetailUiState(
    val uiNftSimpleInfo: UiNftSimpleInfo = UiNftSimpleInfo(),
    val nftList: List<Long> = emptyList(),
    val dataState: DataState = DataState.BEFORE
)

sealed class ExchangeNftDetailEvent {
    object NavigateToBack : ExchangeNftDetailEvent()
    data class ShowExchangeDialog(val point: Int) : ExchangeNftDetailEvent()
    object ShowLoading : ExchangeNftDetailEvent()
    object DismissLoading : ExchangeNftDetailEvent()
    data class ShowToastMessage(val msg: String) : ExchangeNftDetailEvent()
    data class ShowCustomSnack(val msg: String) : ExchangeNftDetailEvent()
}


@HiltViewModel
class ExchangeNftDetailViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    companion object {
        const val NFT_002 = "NFT_002"
    }

    private val _uiState = MutableStateFlow(ExchangeNftDetailUiState())
    val uiState: StateFlow<ExchangeNftDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ExchangeNftDetailEvent>()
    val event: SharedFlow<ExchangeNftDetailEvent> = _event.asSharedFlow()

    private var grade = ""

    fun setGrade(gd: String) {
        grade = gd
        getInitNft()
    }

    private fun getInitNft() {
        viewModelScope.launch {
            nftRepository.getNftForExchange(grade).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiNftSimpleInfo = it.body.toUiNftSimpleInfo(),
                                nftList = uiState.value.nftList + it.body.nftId,
                                dataState = DataState.HAVE_DATA
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _event.emit(ExchangeNftDetailEvent.ShowCustomSnack(it.msg))
                        if (it.code == NFT_002) {
                            _event.emit(ExchangeNftDetailEvent.NavigateToBack)
                        }
                    }
                }
            }
        }
    }

    fun getRefreshNft() {
        viewModelScope.launch {
            nftRepository.getNftForExchangeRefresh(grade, uiState.value.nftList).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                uiNftSimpleInfo = it.body.toUiNftSimpleInfo(),
                                nftList = uiState.value.nftList + it.body.nftId
                            )
                        }
                    }

                    is BaseState.Error -> _event.emit(ExchangeNftDetailEvent.ShowCustomSnack(it.msg))
                }
            }
        }
    }

    fun exchangeNft() {
        viewModelScope.launch {
            nftRepository.exchangeNft(uiState.value.uiNftSimpleInfo.nftId).let {
                when (it) {
                    is BaseState.Success -> {
                        _event.emit(ExchangeNftDetailEvent.ShowLoading)
                    }

                    is BaseState.Error -> _event.emit(ExchangeNftDetailEvent.ShowCustomSnack(it.msg))
                }
            }
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(ExchangeNftDetailEvent.NavigateToBack)
        }
    }

    fun showExchangeDialog() {
        viewModelScope.launch {
            _event.emit(
                ExchangeNftDetailEvent.ShowExchangeDialog(
                    when (grade) {
                        "BASIC" -> 500
                        "STANDARD" -> 750
                        "PREMIUM" -> 1000
                        else -> 500
                    }
                )
            )
        }
    }
}