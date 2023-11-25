package com.aoztg.greengrim.presentation.ui

import androidx.navigation.NavController
import com.aoztg.greengrim.MainNavDirections


internal fun NavController.toAttendCheck() {
    val action = MainNavDirections.actionGlobalToAttendCheckFragment()
    this.navigate(action)
}

internal fun NavController.toChallengeDetail(id: Int) {
    val action = MainNavDirections.actionGlobalToChallengeDetailFragment(id)
    this.navigate(action)
}

internal fun NavController.toCertificationDetail(certificationId: Int) {
    val action = MainNavDirections.actionGlobalToCertificationDetail(certificationId)
    this.navigate(action)
}