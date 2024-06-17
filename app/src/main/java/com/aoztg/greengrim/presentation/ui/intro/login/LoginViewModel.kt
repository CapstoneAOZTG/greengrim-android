package com.aoztg.greengrim.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.config.KeyDataStoreManager
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class LoginUiState(
    val loginState: LoginState = LoginState.Empty
)

sealed class LoginState {
    object Empty : LoginState()
    object Success : LoginState()
    object NoMember : LoginState()
    data class Error(val msg: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MemberRepository,
    private val keyDataStoreManager: KeyDataStoreManager
) : ViewModel() {

    companion object {
        const val UNAVAILABLE_EMAIL = "GLOBAL_001"
        const val UNAVAILABLE_MEMEBER = "MEMBER_001"
        const val DUPLICATE_MEMEBER = "MEMBER_002"
        const val DUPLICATE_NICK = "MEMBER_003"
        const val UNREGISTERED_EMAIL = "MEMBER_004"
    }

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun startLogin(
        email: String,
        socialType: String
    ) {
        viewModelScope.launch {

            keyDataStoreManager.putSocialType(socialType)

            repository.login(
                LoginRequest(
                    email = email,
                    App.fcmToken
                )
            ).let {
                when (it) {
                    is BaseState.Success -> {
                        keyDataStoreManager.putAccessToken(it.body.tokenInfo.accessToken)
                        keyDataStoreManager.putRefreshToken(it.body.tokenInfo.refreshToken)
                        keyDataStoreManager.putMemberId(it.body.tokenInfo.memberId)

                        _uiState.update { state ->
                            state.copy(
                                loginState = LoginState.Success
                            )
                        }
                    }

                    is BaseState.Error -> {
                        when (it.code) {
                            UNAVAILABLE_EMAIL -> _uiState.update { state ->
                                state.copy(
                                    loginState = LoginState.Error(it.msg)
                                )
                            }

                            UNREGISTERED_EMAIL -> _uiState.update { state ->
                                state.copy(
                                    loginState = LoginState.NoMember
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}