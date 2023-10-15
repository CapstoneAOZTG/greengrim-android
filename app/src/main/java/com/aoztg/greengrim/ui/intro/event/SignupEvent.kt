package com.aoztg.greengrim.ui.intro.event


sealed class SignupEvent {
    object NavigateToMainActivity : SignupEvent()
    object NavigateToGallery : SignupEvent()
}