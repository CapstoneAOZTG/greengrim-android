package com.aoztg.greengrim.presentation.ui.intro.event

sealed class IntroEvent {
    object NavigateToMainActivity : IntroEvent()
    object NavigateToTermsFragment : IntroEvent()
    object NavigateToSignupFragment : IntroEvent()
    object NavigateToGallery : IntroEvent()
}
