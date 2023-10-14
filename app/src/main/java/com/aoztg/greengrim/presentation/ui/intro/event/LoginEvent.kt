package com.aoztg.greengrim.presentation.ui.intro.event

sealed class LoginEvent {
    object NavigateToMainActivity : LoginEvent()
    object NavigateToTermsActivity : LoginEvent()
}