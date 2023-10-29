package com.aoztg.greengrim.presentation.ui.challenge.list

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChallengeListViewModel @Inject constructor(): ViewModel() {

    private val _challengeRoom = MutableStateFlow<List<ChallengeRoom>>(emptyList())
    val challengeRoom: StateFlow<List<ChallengeRoom>> = _challengeRoom.asStateFlow()

    fun getChallengeRooms(){

        _challengeRoom.value = listOf(
            ChallengeRoom("test","title", listOf("a","b","c")),
            ChallengeRoom("test","title", listOf("a","b","c")),
            ChallengeRoom("test","title", listOf("a","b","c")),
            ChallengeRoom("test","title", listOf("a","b","c")),
            ChallengeRoom("test","title", listOf("a","b","c")),
        )
    }
}