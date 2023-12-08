package com.aoztg.greengrim.presentation.ui.global.setwalletpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.repository.NftRepository
import com.aoztg.greengrim.presentation.ui.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class SetWalletPasswordEvents {
    data class TypePassword(val idx: Int) : SetWalletPasswordEvents()
    data class ErasePassword(val idx: Int) : SetWalletPasswordEvents()
    object ChangeModeToCheck : SetWalletPasswordEvents()
    object RemovePasswordViews : SetWalletPasswordEvents()
    data class ShowToastMessage(val msg: String) : SetWalletPasswordEvents()
    data class ShowSnackMessage(val msg: String) : SetWalletPasswordEvents()
    object NavigateToBack : SetWalletPasswordEvents()
    object ShowLoading : SetWalletPasswordEvents()
    object DismissLoading : SetWalletPasswordEvents()
}

data class SetWalletPasswordUiState(
    val isCheckMode: Boolean = false,
    val passwordCheckState: BaseUiState = BaseUiState.Empty
)

@HiltViewModel
class SetWalletPasswordViewModel @Inject constructor(
    private val nftRepository: NftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetWalletPasswordUiState())
    val uiState: StateFlow<SetWalletPasswordUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<SetWalletPasswordEvents>()
    val events: SharedFlow<SetWalletPasswordEvents> = _events.asSharedFlow()

    private val _curPassword = MutableStateFlow("")
    val curPassword: StateFlow<String> = _curPassword.asStateFlow()

    private var originPassword = ""

    fun typePassword(text: String) {
        if (curPassword.value.length < 5) {
            _curPassword.value = curPassword.value + text
            viewModelScope.launch {
                _events.emit(SetWalletPasswordEvents.TypePassword(curPassword.value.length - 1))
            }
        } else if (curPassword.value.length == 5) {
            _curPassword.value = curPassword.value + text
            if (!uiState.value.isCheckMode) {
                changeModeToCheck()
            }
        }
    }

    fun erasePassword() {
        if (curPassword.value.isNotEmpty()) {
            _curPassword.value = curPassword.value.substring(0, curPassword.value.length - 1)
            viewModelScope.launch {
                _events.emit(SetWalletPasswordEvents.ErasePassword(curPassword.value.length))
            }
        }
    }

    private fun changeModeToCheck() {
        _uiState.update { state ->
            state.copy(
                isCheckMode = true
            )
        }
        originPassword = curPassword.value
        _curPassword.value = ""
        observePasswordCheck()
        viewModelScope.launch {
            _events.emit(SetWalletPasswordEvents.ChangeModeToCheck)
        }
    }

    private fun observePasswordCheck() {
        _curPassword.onEach {
            if (it.length == 6) {
                if (it == originPassword) {
                    _events.emit(SetWalletPasswordEvents.ShowLoading)
                    nftRepository.createWallet(CreateWalletRequest(originPassword)).let { state ->
                        when (state) {
                            is BaseState.Success -> {
                                _events.emit(SetWalletPasswordEvents.ShowToastMessage("지갑이 생성되었습니다"))
                                navigateToBack()
                            }

                            is BaseState.Error -> {
                                _events.emit(SetWalletPasswordEvents.ShowSnackMessage(state.msg))
                            }
                        }
                    }
                    _events.emit(SetWalletPasswordEvents.DismissLoading)
                } else {
                    _curPassword.value = ""
                    _uiState.update { state ->
                        state.copy(
                            passwordCheckState = BaseUiState.Error("비밀번호와 일치하지 않습니다")
                        )
                    }
                    _events.emit(SetWalletPasswordEvents.RemovePasswordViews)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun navigateToBack() {
        viewModelScope.launch {
            _events.emit(SetWalletPasswordEvents.NavigateToBack)
        }
    }
}