package com.aoztg.greengrim.presentation.ui.mypage.mywallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.WalletInfoRequest
import com.aoztg.greengrim.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EditWalletEvent{
    object NavigateToBack : EditWalletEvent()
    data class ShowCustomSnack(val msg: String) : EditWalletEvent()
    object NavigateToMyPage : EditWalletEvent()
    data class ShowToastMessage(val msg: String) : EditWalletEvent()
}

@HiltViewModel
class EditWalletViewModel @Inject constructor(
    private val repository : MemberRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<EditWalletEvent>()
    val event : SharedFlow<EditWalletEvent> = _event.asSharedFlow()

    private var originWalletName = ""
    private var originWalletAddress = ""

    private val getWalletData = MutableStateFlow(false)
    val walletName = MutableStateFlow("")
    val walletAddress = MutableStateFlow("")

    val isDataReady = combine(getWalletData, walletName, walletAddress) { flag, name, address ->
        flag && (name != originWalletName || address != originWalletAddress) && name.isNotBlank() && address.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun getWalletInfo(){
        viewModelScope.launch {

            repository.getMyWalletInfo().let{
                when(it){
                    is BaseState.Success -> {
                        originWalletName = it.body.name ?: ""
                        originWalletAddress = it.body.address ?: ""
                        walletName.value = it.body.name ?: ""
                        walletAddress.value = it.body.address ?: ""
                        getWalletData.value = true
                    }
                    is BaseState.Error -> {
                        _event.emit(EditWalletEvent.ShowCustomSnack(it.msg))
                    }
                }
            }
        }
    }

    fun editWallet(){
        viewModelScope.launch {
            repository.editWallet(WalletInfoRequest(walletName.value, walletAddress.value)).let{
                when(it){
                    is BaseState.Success -> {
                        _event.emit(EditWalletEvent.ShowToastMessage("지갑정보 수정 완료"))
                        _event.emit(EditWalletEvent.NavigateToMyPage)
                    }

                    is BaseState.Error -> {
                        _event.emit(EditWalletEvent.ShowCustomSnack(it.msg))
                    }
                }
            }
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(EditWalletEvent.NavigateToBack)
        }
    }

}