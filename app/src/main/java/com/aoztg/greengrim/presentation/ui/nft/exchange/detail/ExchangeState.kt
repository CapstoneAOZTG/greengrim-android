package com.aoztg.greengrim.presentation.ui.nft.exchange.detail

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

object ExchangeState {

    val exchangeState = MutableSharedFlow<Boolean>()

    fun exchangeSuccess() {
        CoroutineScope(Dispatchers.Main).launch {
            exchangeState.emit(true)
        }
    }

    fun exchangeFailure() {
        CoroutineScope(Dispatchers.Main).launch {
            exchangeState.emit(false)
        }
    }
}