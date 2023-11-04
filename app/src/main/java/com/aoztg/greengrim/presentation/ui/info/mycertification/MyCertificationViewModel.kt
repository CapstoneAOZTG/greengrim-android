package com.aoztg.greengrim.presentation.ui.info.mycertification

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.presentation.ui.info.model.MyCertification
import com.aoztg.greengrim.presentation.util.toHeaderText
import com.aoztg.greengrim.presentation.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class MyCertificationUiState(
    val curMonth: MonthState = MonthState.Empty,
    val curDate: DateState = DateState.Empty,
    val eventDateList: List<LocalDate> = emptyList(),
    val certificationList: List<MyCertification> = emptyList()
)

sealed class MonthState {
    object Empty : MonthState()
    data class Changed(val stringMonth: String, val originMonth: YearMonth) : MonthState()
}

sealed class DateState {
    object Empty : DateState()
    data class Changed(val stringDate: String, val originDate: LocalDate) : DateState()
}

@HiltViewModel
class MyCertificationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MyCertificationUiState())
    val uiState: StateFlow<MyCertificationUiState> = _uiState.asStateFlow()

    fun scrollMonth(date: YearMonth) {
        val stringYearMonth = date.year.toString() + "년 " + date.monthValue + "월"
        _uiState.update { state ->
            state.copy(
                curMonth = MonthState.Changed(stringYearMonth, date)
            )
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                curDate = DateState.Changed(date.toHeaderText(), date)
            )
        }

        getCurDateCertification(date)
    }

    fun getEventList() {
        _uiState.update { state ->
            state.copy(
                eventDateList = listOf(
                    "2023-10-20".toLocalDate(),
                    "2023-10-30".toLocalDate(),
                )
            )
        }
    }

    private fun getCurDateCertification(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                certificationList = listOf(
                    MyCertification(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        category = "줍킹",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                    MyCertification(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        category = "줍킹",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                    MyCertification(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        category = "줍킹",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                    MyCertification(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        title = "인하대학교 쓰레기 줍기",
                        category = "줍킹",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                )
            )
        }

    }

}