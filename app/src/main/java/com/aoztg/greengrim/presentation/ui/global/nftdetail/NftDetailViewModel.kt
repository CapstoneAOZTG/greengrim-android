package com.aoztg.greengrim.presentation.ui.global.nftdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.global.mapper.toUiNftDetail
import com.aoztg.greengrim.presentation.ui.global.model.UiNftDetail
import com.aoztg.greengrim.presentation.ui.global.nftdetail.NftDetailFragment.Companion.PURCHASE
import com.aoztg.greengrim.presentation.ui.global.nftdetail.NftDetailFragment.Companion.SELL
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

data class NftDetailUiState(
    val nftDetail: UiNftDetail = UiNftDetail()
)

sealed class NftDetailEvents {
    data class ShowSnackMessage(val msg: String) : NftDetailEvents()
    object NavigateToBack: NftDetailEvents()
}

@HiltViewModel
class NftDetailViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NftDetailUiState())
    val uiState: StateFlow<NftDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NftDetailEvents>()
    val events: SharedFlow<NftDetailEvents> = _events.asSharedFlow()

    val btnState = MutableStateFlow("")

    private var nftId = -1L

    fun checkWallet(target: Int){
        viewModelScope.launch {
            nftRepository.checkWalletExist().let{
                when(it){
                    is BaseState.Success -> {
                        if(it.body.existed){
                        } else {
                            _events.emit(NftDetailEvents.ShowSnackMessage("지갑을 먼저 생성해주세요!"))
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(NftDetailEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    private fun getNftDetail() {
        viewModelScope.launch {
            nftRepository.getNftDetail(nftId).let {
                when (it) {
                    is BaseState.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                nftDetail = it.body.toUiNftDetail()
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(NftDetailEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
        }
    }

    fun setNftId(id: Long) {
        nftId = id
        getNftDetail()
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(NftDetailEvents.NavigateToBack)
        }
    }
}