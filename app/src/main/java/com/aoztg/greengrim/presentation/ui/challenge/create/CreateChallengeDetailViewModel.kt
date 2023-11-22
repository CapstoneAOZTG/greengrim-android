package com.aoztg.greengrim.presentation.ui.challenge.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.ChallengeRepository
import com.aoztg.greengrim.data.repository.ChatRepository
import com.aoztg.greengrim.presentation.ui.BaseState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class CreateChallengeDetailUiState(
    val certificateProgressState: ProgressState = ProgressState.Empty,
    val ticketProgressState: ProgressState = ProgressState.Empty,
    val minCertificateProgressState: ProgressState = ProgressState.Empty,
    val randomKeywordState: KeywordState = KeywordState.Empty,
    val createChallengeState: BaseState = BaseState.Empty
)

sealed class ProgressState {
    object Empty : ProgressState()
    data class Changed(val text: String) : ProgressState()
}

sealed class KeywordState {
    object Empty : KeywordState()
    data class Set(val keywords: List<String>) : KeywordState()
}

sealed class CreateChallengeDetailEvents{
    object NavigateToBack : CreateChallengeDetailEvents()
    data class NavigateToChatList(val chatId: Int) : CreateChallengeDetailEvents()
}

@HiltViewModel
class CreateChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    companion object {
        val keywordsWhat = listOf("꽃다발", "개", "고양이", "버스", "감자튀김", "컵", "운동화", "기타", "모자", "소파", "우주선", "접시", "나무", "럭비공", "모래성", "초콜릿", "피망", "책", "포도")
        val keywordsPlace = listOf("공원", "바다", "숲", "도시", "부엌", "모래사장", "농장", "학교", "커피숍", "동굴", "운동장", "다리", "회사", "골목", "절벽", "슈퍼마켓", "우주", "지하철", "박물관", "언덕",)
    }

    private val _uiState = MutableStateFlow(CreateChallengeDetailUiState())
    val uiState: StateFlow<CreateChallengeDetailUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CreateChallengeDetailEvents>()
    val events: SharedFlow<CreateChallengeDetailEvents> = _events.asSharedFlow()

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val imageUrl = MutableStateFlow("")
    val keyword = MutableStateFlow("")
    val category = MutableStateFlow("")

    val certificateProgress = MutableStateFlow(0)
    val ticketProgress = MutableStateFlow(0)
    val minCertificateProgress = MutableStateFlow(0)

    private var goalCount = 0
    private var ticketTotalCount = 0
    private var weekMinCount = 0

    val isDataReady = combine(title, description, imageUrl, keyword, category) {
            title, description, img, keyword, category ->
        title.isNotBlank() && description.isNotBlank()
                && img.isNotBlank() && keyword.isNotBlank() && category.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setImageUrl(url: String) {
        imageUrl.value = url
    }

    fun setKeyword(text: String) {
        keyword.value = text
    }

    init {
        observeSeekBar()
        setRandomKeywords()
    }

    private fun observeSeekBar() {
        certificateProgress.onEach { progress ->
            goalCount = 10 + progress * 5
            _uiState.update { state ->
                state.copy(
                    certificateProgressState = ProgressState.Changed(
                        goalCount.toString() + "회"
                    ),
                    randomKeywordState = KeywordState.Empty
                )
            }
        }.launchIn(viewModelScope)

        ticketProgress.onEach { progress ->
            ticketTotalCount = 20 + progress * 20
            _uiState.update { state ->
                state.copy(
                    ticketProgressState = ProgressState.Changed(
                        ticketTotalCount.toString() + "개"
                    ),
                    randomKeywordState = KeywordState.Empty
                )
            }
        }.launchIn(viewModelScope)

        minCertificateProgress.onEach { progress ->
            weekMinCount = 2 + progress
            _uiState.update { state ->
                state.copy(
                    minCertificateProgressState = ProgressState.Changed(
                        "주 " + weekMinCount.toString() + "회"
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

    fun setCategory(data: String){
        category.value = data
    }

    fun navigateToBack(){
        viewModelScope.launch{
            _events.emit(CreateChallengeDetailEvents.NavigateToBack)
        }
    }

    fun createChallenge() {
        viewModelScope.launch {

            val response = challengeRepository.createChallenge(
                CreateChallengeRequest(
                    category = category.value,
                    title = title.value,
                    description = description.value,
                    imgUrl = imageUrl.value,
                    goalCount = goalCount,
                    ticketTotalCount = ticketTotalCount,
                    weekMinCount = weekMinCount,
                    keyword = keyword.value)
            )

            if(response.isSuccessful){
                response.body()?.let{
                    _events.emit(CreateChallengeDetailEvents.NavigateToChatList(it.id))
                }
            } else {

                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                _uiState.update { state ->
                    state.copy(
                        createChallengeState = BaseState.Error(error.message)
                    )
                }

            }
        }

    }


}