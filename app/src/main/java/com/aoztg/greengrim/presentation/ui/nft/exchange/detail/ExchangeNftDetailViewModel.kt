package com.aoztg.greengrim.presentation.ui.nft.exchange.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftSimpleInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



data class ExchangeNftDetailUiState(
    val uiNftSimpleInfo : UiNftSimpleInfo = UiNftSimpleInfo(),
    val nftList : List<Int> = emptyList()
)


@HiltViewModel
class ExchangeNftDetailViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeNftDetailUiState())
    val uiState: StateFlow<ExchangeNftDetailUiState> = _uiState.asStateFlow()

    private var grade = ""

    fun setGrade(gd : String){
        grade = gd
        getInitNft()
    }

    private fun getInitNft(){
        viewModelScope.launch {
            nftRepository.getNftForExchange(grade).let{
                when(it){
                    is BaseState.Success -> {

                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }

    fun getRefreshNft(){
        viewModelScope.launch {
            nftRepository.getNftForExchangeRefresh(grade,uiState.value.nftList).let{
                when(it){
                    is BaseState.Success -> {

                    }

                    is BaseState.Error -> {

                    }
                }
            }
        }
    }
}