package com.aoztg.greengrim.presentation.ui.intro.event


sealed class SignupEvent {
    object NavigateToMainActivity : SignupEvent()
    object NavigateToGallery : SignupEvent()
    object ShowWarning : SignupEvent()
    object HideWarning : SignupEvent()
}