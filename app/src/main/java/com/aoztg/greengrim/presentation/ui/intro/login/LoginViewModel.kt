package com.aoztg.greengrim.presentation.ui.intro.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.repository.IntroRepository
import com.aoztg.greengrim.presentation.util.Constants
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
class LoginViewModel @Inject constructor(private val introRepository: IntroRepository) :
    ViewModel() {


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
        email: String
    ) {
        viewModelScope.launch {

            introRepository.login(
                LoginRequest(
                    email = email,
                    App.fcmToken
                )
            ).let{
                when(it){
                    is BaseState.Success -> {
                        App.sharedPreferences.edit()
                            .putString(Constants.X_ACCESS_TOKEN, it.body.accessToken)
                            .putString(Constants.X_REFRESH_TOKEN, it.body.refreshToken)
                            .putLong(Constants.MEMBER_ID, it.body.memberId)
                            .apply()

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