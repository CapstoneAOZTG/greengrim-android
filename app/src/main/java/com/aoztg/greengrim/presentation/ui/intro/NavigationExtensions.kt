package com.aoztg.greengrim.presentation.ui.intro

import androidx.navigation.NavController
import com.aoztg.greengrim.presentation.ui.intro.view.LoginFragmentDirections
import com.aoztg.greengrim.presentation.ui.intro.view.TermsFragmentDirections

internal fun NavController.navigateToTerms() {
    val action = LoginFragmentDirections.actionLoginFragmentToTermsFragment()
    this.navigate(action)
}

internal fun NavController.navigateToSignup() {
    val action = TermsFragmentDirections.actionTermsFragmentToSignupFragment()
    this.navigate(action)
}