package com.aoztg.greengrim.presentation.ui

import androidx.navigation.NavController
import com.aoztg.greengrim.MainNavDirections


internal fun NavController.toAttendCheck() {
    val action = MainNavDirections.actionGlobalToAttendCheckFragment()
    navigate(action)
}

internal fun NavController.toChallengeDetail(id: Long) {
    val action = MainNavDirections.actionGlobalToChallengeDetailFragment(id)
    navigate(action)
}

internal fun NavController.toCertificationDetail(certificationId: Long) {
    val action = MainNavDirections.actionGlobalToCertificationDetail(certificationId)
    navigate(action)
}

internal fun NavController.toNftDetail(nftId: Long){
    val action = MainNavDirections.actionGlobalToNftDetailFragment(nftId)
    navigate(action)
}

internal fun NavController.toWebView(url: String){
    val action = MainNavDirections.actionGlobalToWebviewFragment(url)
    navigate(action)
}

internal fun NavController.toProfile(id: Long){
    val action = MainNavDirections.actionGlobalToProfileFragment(id)
    navigate(action)
}

internal fun NavController.toChallengeCategory(){
    val action = MainNavDirections.actionGlobalToChallengeCategoryFragment()
    navigate(action)
}
