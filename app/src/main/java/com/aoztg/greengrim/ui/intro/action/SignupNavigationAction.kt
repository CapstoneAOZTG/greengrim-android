package com.aoztg.greengrim.ui.intro.action


sealed class SignupNavigationAction {
    object NavigateToMainActivity : SignupNavigationAction()
    object NavigateToGallery : SignupNavigationAction()
}