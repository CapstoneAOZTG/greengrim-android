package com.aoztg.greengrim.presentation.ui.info.mywallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyWalletUiState(
    val amount: String = "",
    val link: String = ""
)

sealed class MyWalletEvents {
    data class ShowSnackMessage(val msg: String) : MyWalletEvents()
    data class ShowToastMessage(val msg: String) : MyWalletEvents()
    data class CopyClipBoard(val link: String) : MyWalletEvents()
    object NavigateToBack : MyWalletEvents()
    object ShowLoading : MyWalletEvents()
    object DismissLoading : MyWalletEvents()
}

@HiltViewModel
class MyWalletViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyWalletUiState())
    val uiState: StateFlow<MyWalletUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyWalletEvents>()
    val events: SharedFlow<MyWalletEvents> = _events.asSharedFlow()

    private var originLink = ""

    fun getMyWalletInfo() {
        viewModelScope.launch {
            _events.emit(MyWalletEvents.ShowLoading)
            nftRepository.getWalletInfo().let {
                when (it) {
                    is BaseState.Success -> {
                        val body = it.body
                        originLink = body.address
                        _uiState.update { state ->
                            state.copy(
                                amount = body.klay.toString(),
                                link = body.address.substring(0..30) + ".."
                            )
                        }
                    }

                    is BaseState.Error -> {
                        _events.emit(MyWalletEvents.ShowSnackMessage(it.msg))
                    }
                }
            }
            delay(200)
            _events.emit(MyWalletEvents.DismissLoading)
        }
    }

    fun copyClipBoard() {
        viewModelScope.launch {
            _events.emit(MyWalletEvents.CopyClipBoard(originLink))
        }
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(MyWalletEvents.NavigateToBack)
        }
    }

}