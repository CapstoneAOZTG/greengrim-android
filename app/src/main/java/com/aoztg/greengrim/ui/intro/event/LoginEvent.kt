package com.aoztg.greengrim.ui.intro.event

sealed class LoginEvent {

    object NavigateToMainActivity : LoginEvent()
    class NavigateToTermsActivity(val email : String) : LoginEvent()
}