package com.aoztg.greengrim.presentation.ui.catchgame

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class CatchInGameUiState(
    val level: Int = 0,
    val score: Int = 0,
    val time: Int = 100
)


@HiltViewModel
class CatchInGameViewModel @Inject constructor(): ViewModel() {


}