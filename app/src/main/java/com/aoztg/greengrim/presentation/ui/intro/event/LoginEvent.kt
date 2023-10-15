package com.aoztg.greengrim.presentation.ui.intro.event

sealed class LoginEvent {

    object NavigateToMainActivity : LoginEvent()
    class NavigateToTermsActivity(val email : String) : LoginEvent()
}