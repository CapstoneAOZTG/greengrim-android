package com.aoztg.greengrim.ui.login

sealed class LoginNavigationAction {

    object NavigateToMainActivity : LoginNavigationAction()
    class NavigateToTermsActivity(val email : String) : LoginNavigationAction()
}