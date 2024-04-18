package com.aoztg.greengrim.presentation.ui.nft.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.nft.nftcollection.NftCollectionFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ExchangeNftEvent {
    object NavigateToBack : ExchangeNftEvent()
    data class NavigateToExchangeDetail(val grade: String) : ExchangeNftEvent()
}

@HiltViewModel
class ExchangeNftViewModel @Inject constructor() : ViewModel() {

    val curFilter = MutableStateFlow(NftCollectionFilter.BASIC)

    private val _event = MutableSharedFlow<ExchangeNftEvent>()
    val event: SharedFlow<ExchangeNftEvent> = _event.asSharedFlow()

    fun changeFilter(filter: NftCollectionFilter) {
        curFilter.update { filter }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _event.emit(ExchangeNftEvent.NavigateToBack)
        }
    }

    fun navigateToExchangeDetail() {
        viewModelScope.launch {
            _event.emit(ExchangeNftEvent.NavigateToExchangeDetail(curFilter.value.text))
        }
    }

}