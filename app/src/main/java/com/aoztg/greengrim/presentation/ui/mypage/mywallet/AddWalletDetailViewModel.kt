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

sealed class AddWalletDetailEvent{
    object NavigateToBack : AddWalletDetailEvent()
    data class ShowCustomSnack(val msg: String) : AddWalletDetailEvent()
    object NavigateToMyPage : AddWalletDetailEvent()
}

@HiltViewModel
class AddWalletDetailViewModel @Inject constructor(
    private val repository : MemberRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<AddWalletDetailEvent>()
    val event : SharedFlow<AddWalletDetailEvent> = _event.asSharedFlow()

    val walletName = MutableStateFlow("")
    val walletAddress = MutableStateFlow("")

    val isDataReady = combine(walletName, walletAddress) { name, address ->
        name.isNotBlank() && address.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun addWallet(){
       viewModelScope.launch {
           repository.addWallet(WalletInfoRequest(walletName.value, walletAddress.value)).let{
               when(it){
                   is BaseState.Success -> {
                        _event.emit(AddWalletDetailEvent.NavigateToMyPage)
                   }

                   is BaseState.Error -> {
                        _event.emit(AddWalletDetailEvent.ShowCustomSnack(it.msg))
                   }
               }
           }
       }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _event.emit(AddWalletDetailEvent.NavigateToBack)
        }
    }


}