package com.aoztg.greengrim.ui.intro.event


sealed class SignupNavigationAction {
    object NavigateToMainActivity : SignupNavigationAction()
    object NavigateToGallery : SignupNavigationAction()
}