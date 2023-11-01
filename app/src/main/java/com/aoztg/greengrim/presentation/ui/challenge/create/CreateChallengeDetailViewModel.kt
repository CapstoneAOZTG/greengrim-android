package com.aoztg.greengrim.presentation.ui.challenge.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class CreateChallengeDetailUiState(
    val certificateProgressState: ProgressState = ProgressState.Empty,
    val ticketProgressState: ProgressState = ProgressState.Empty,
    val minCertificateProgressState: ProgressState = ProgressState.Empty,
    val randomKeywordState: KeywordState = KeywordState.Empty
)

sealed class ProgressState {
    object Empty : ProgressState()
    data class Changed(val text: String) : ProgressState()
}

sealed class KeywordState {
    object Empty : KeywordState()
    data class Set(val keywords: List<String>) : KeywordState()
}

@HiltViewModel
class CreateChallengeDetailViewModel @Inject constructor() : ViewModel() {

    companion object {
        val keywordsWhat = listOf("꽃다발", "개", "고양이", "버스", "감자튀김", "컵", "운동화", "기타", "모자", "소파", "우주선", "접시", "나무", "럭비공", "모래성", "초콜릿", "피망", "책", "포도")
        val keywordsPlace = listOf("공원", "바다", "숲", "도시", "부엌", "모래사장", "농장", "학교", "커피숍", "동굴", "운동장", "다리", "회사", "골목", "절벽", "슈퍼마켓", "우주", "지하철", "박물관", "언덕",)
    }

    private val _uiState = MutableStateFlow(CreateChallengeDetailUiState())
    val uiState: StateFlow<CreateChallengeDetailUiState> = _uiState.asStateFlow()

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    private var imageUrl = ""
    private var keyword = ""

    val certificateProgress = MutableStateFlow(0)
    val ticketProgress = MutableStateFlow(0)
    val minCertificateProgress = MutableStateFlow(0)

    val isDataReady = combine(title, description) { title, description ->
        title.isNotBlank() && description.isNotBlank()
                && imageUrl.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setImageUrl(url: String) {
        imageUrl = url
    }

    fun setKeyword(text: String) {
        keyword = text
    }

    init {
        observeSeekBar()
        setRandomKeywords()
    }

    private fun observeSeekBar() {
        certificateProgress.onEach { progress ->
            _uiState.update { state ->
                state.copy(
                    certificateProgressState = ProgressState.Changed(
                        (10 + (progress * 5)).toString() + "회"
                    ),
                    randomKeywordState = KeywordState.Empty
                )
            }
        }.launchIn(viewModelScope)

        ticketProgress.onEach { progress ->
            _uiState.update { state ->
                state.copy(
                    ticketProgressState = ProgressState.Changed(
                        (20 + (progress * 20)).toString() + "개"
                    ),
                    randomKeywordState = KeywordState.Empty
                )
            }
        }.launchIn(viewModelScope)

        minCertificateProgress.onEach { progress ->
            _uiState.update { state ->
                state.copy(
                    minCertificateProgressState = ProgressState.Changed(
                        "주 " + (2 + progress).toString() + "회"
                    ),
                    randomKeywordState = KeywordState.Empty
                )
            }
        }.launchIn(viewModelScope)
    }

    fun setRandomKeywords() {

        val randKeywords = mutableListOf<String>()
        while (randKeywords.size != 5) {
            val keyword = keywordsWhat.random()
            if (keyword !in randKeywords) randKeywords.add(keyword)
        }

        _uiState.update { state ->
            state.copy(
                randomKeywordState = KeywordState.Set(randKeywords.toList())
            )
        }
    }

    fun createChallenge() {

    }


}