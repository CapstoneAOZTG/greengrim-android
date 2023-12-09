package com.aoztg.greengrim.presentation.ui.global.checkpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class CheckPasswordEvents {
    data class TypePassword(val idx: Int) : CheckPasswordEvents()
    data class ErasePassword(val idx: Int) : CheckPasswordEvents()
    object RemovePasswordViews : CheckPasswordEvents()
    data class ShowToastMessage(val msg: String) : CheckPasswordEvents()
    data class ShowSnackMessage(val msg: String) : CheckPasswordEvents()
    object NavigateToBack : CheckPasswordEvents()
    object ShowLoading : CheckPasswordEvents()
    object DismissLoading : CheckPasswordEvents()
}

@HiltViewModel
class CheckPasswordViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {


    private val _events = MutableSharedFlow<CheckPasswordEvents>()
    val events: SharedFlow<CheckPasswordEvents> = _events.asSharedFlow()

    private val _curPassword = MutableStateFlow("")
    val curPassword: StateFlow<String> = _curPassword.asStateFlow()

    fun typePassword(text: String) {
        if (curPassword.value.length < 5) {
            _curPassword.value = curPassword.value + text
            viewModelScope.launch {
                _events.emit(CheckPasswordEvents.TypePassword(curPassword.value.length - 1))
            }
        } else if (curPassword.value.length == 5) {
            _curPassword.value = curPassword.value + text
        }
    }

    fun erasePassword() {
        if (curPassword.value.isNotEmpty()) {
            _curPassword.value = curPassword.value.substring(0, curPassword.value.length - 1)
            viewModelScope.launch {
                _events.emit(CheckPasswordEvents.ErasePassword(curPassword.value.length))
            }
        }
    }
}