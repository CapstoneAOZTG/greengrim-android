package com.aoztg.greengrim.presentation.ui

import androidx.navigation.NavController
import com.aoztg.greengrim.MainNavDirections


internal fun NavController.toAttendCheck(){
    val action = MainNavDirections.actionGlobalToAttendCheckFragment()
    this.navigate(action)
}