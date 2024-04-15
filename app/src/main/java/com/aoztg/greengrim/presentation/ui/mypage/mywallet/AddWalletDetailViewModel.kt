package com.aoztg.greengrim.presentation.ui.mypage.mywallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class AddWalletEvent{
    object NavigateToBack : AddWalletEvent()
}

@HiltViewModel
class AddWalletDetailViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<AddWalletEvent>()
    val event : SharedFlow<AddWalletEvent> = _event.asSharedFlow()

    val walletName = MutableStateFlow("")
    val walletAddress = MutableStateFlow("")

    val isDataReady = combine(walletName, walletAddress) { name, address ->
        name.isNotBlank() && address.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )


}