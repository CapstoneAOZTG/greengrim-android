package com.aoztg.greengrim.ui.intro.action

sealed class LoginNavigationAction {

    object NavigateToMainActivity : LoginNavigationAction()
    class NavigateToTermsActivity(val email : String) : LoginNavigationAction()
}