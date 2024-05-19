package com.aoztg.greengrim.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.config.KeyDataStoreManager
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashEvent {
    object NavigateToMainActivity : SplashEvent()
    object NavigateToIntroActivity : SplashEvent()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: MemberRepository,
    private val keyDataStoreManager: KeyDataStoreManager
) : ViewModel() {

    private val _event = MutableSharedFlow<SplashEvent>()
    val event: SharedFlow<SplashEvent> = _event.asSharedFlow()

    fun checkLoginType() {
        viewModelScope.launch {
            keyDataStoreManager.getRefreshToken()?.let {
                refreshToken(it)
            } ?: run {
                _event.emit(SplashEvent.NavigateToIntroActivity)
            }
        }
    }

    private fun refreshToken(token: String) {
        viewModelScope.launch {
            repository.refreshToken(token).let {
                when (it) {
                    is BaseState.Success -> {
                        keyDataStoreManager.putAccessToken(it.body.accessToken)
                        keyDataStoreManager.putRefreshToken(it.body.refreshToken)
                        keyDataStoreManager.putMemberId(it.body.memberId)
                        _event.emit(SplashEvent.NavigateToMainActivity)
                    }

                    is BaseState.Error -> {
                        keyDataStoreManager.deleteAccessToken()
                        keyDataStoreManager.deleteRefreshToken()
                        keyDataStoreManager.deleteMemberId()
                        _event.emit(SplashEvent.NavigateToIntroActivity)
                    }
                }
            }
        }
    }
}