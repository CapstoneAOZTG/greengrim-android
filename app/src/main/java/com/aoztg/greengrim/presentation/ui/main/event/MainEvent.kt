package com.aoztg.greengrim.presentation.ui.main.event

sealed class MainEvent {
    object HideBottomNav : MainEvent()
    object ShowBottomNav : MainEvent()
}