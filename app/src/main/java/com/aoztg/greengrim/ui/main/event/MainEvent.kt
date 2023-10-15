package com.aoztg.greengrim.ui.main.event

sealed class MainEvent {
    object HideBottomNav : MainEvent()
    object ShowBottomNav : MainEvent()
}