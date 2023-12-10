package com.aoztg.greengrim.presentation.ui

import androidx.navigation.NavController
import com.aoztg.greengrim.MainNavDirections


internal fun NavController.toAttendCheck() {
    val action = MainNavDirections.actionGlobalToAttendCheckFragment()
    navigate(action)
}

internal fun NavController.toChallengeDetail(id: Int) {
    val action = MainNavDirections.actionGlobalToChallengeDetailFragment(id)
    navigate(action)
}

internal fun NavController.toCertificationDetail(certificationId: Int) {
    val action = MainNavDirections.actionGlobalToCertificationDetail(certificationId)
    navigate(action)
}

internal fun NavController.toWalletPasswordInput(){
    val action = MainNavDirections.actionGlobalToCheckPasswordFragment()
    navigate(action)
}

internal fun NavController.toNftDetail(nftId: Int){
    val action = MainNavDirections.actionGlobalToNftDetailFragment(nftId)
    navigate(action)
}

internal fun NavController.toGrimDetail(grimId: Int){
    val action = MainNavDirections.actionGlobalToPaintDetailFragment(grimId)
    navigate(action)
}

internal fun NavController.toMarket(){
    val action = MainNavDirections.actionGlobalToMarketFragment()
    navigate(action)
}

internal fun NavController.toCreateNft(grimId: Int, grimUrl: String){
    val action = MainNavDirections.actionGlobalToCreateNftFragment(grimId, grimUrl)
    navigate(action)
}