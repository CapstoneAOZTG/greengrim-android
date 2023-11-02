package com.aoztg.greengrim.presentation.ui


sealed class LoadingState {
    object Empty : LoadingState()
    data class IsLoading(val state: Boolean) : LoadingState()
}

sealed class BaseState {
    object Empty : BaseState()
    object Success : BaseState()
    object Failure : BaseState()
    data class Error(val msg: String) : BaseState()
}