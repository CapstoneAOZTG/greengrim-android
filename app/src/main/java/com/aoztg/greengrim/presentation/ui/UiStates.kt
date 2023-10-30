package com.aoztg.greengrim.presentation.ui


sealed class LoadingState {
    object Empty : LoadingState()
    data class IsLoading(val state: Boolean) : LoadingState()
}