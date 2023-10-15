package com.aoztg.greengrim.ui.intro.event

sealed class LoginNavigationAction {

    object NavigateToMainActivity : LoginNavigationAction()
    class NavigateToTermsActivity(val email : String) : LoginNavigationAction()
}