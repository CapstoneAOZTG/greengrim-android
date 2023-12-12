package com.aoztg.greengrim.presentation.ui.catchgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.ChallengeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatchGameViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
): ViewModel() {

    fun postPoint(){
        viewModelScope.launch {
            challengeRepository.postPoint()
        }
    }
}