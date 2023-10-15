package com.aoztg.greengrim.ui.intro.signup


sealed class SignupNavigationAction {
    object NavigateToMainActivity : SignupNavigationAction()
    object NavigateToGallery : SignupNavigationAction()
}