package com.aoztg.greengrim.presentation.ui.chat.certificationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.presentation.ui.DateState
import com.aoztg.greengrim.presentation.ui.MonthState
import com.aoztg.greengrim.presentation.ui.chat.model.CertificationListItem
import com.aoztg.greengrim.presentation.util.toHeaderText
import com.aoztg.greengrim.presentation.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject


data class CertificationListUiState(
    val curMonth: MonthState = MonthState.Empty,
    val curDate: DateState = DateState.Empty,
    val eventDateList: List<LocalDate> = emptyList(),
    val certificationList: List<CertificationListItem> = emptyList()
)

sealed class CertificationListEvents {
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : CertificationListEvents()
}

@HiltViewModel
class CertificationListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CertificationListUiState())
    val uiState: StateFlow<CertificationListUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CertificationListEvents>()
    val events: SharedFlow<CertificationListEvents> = _events.asSharedFlow()

    private var curYear = 1980
    private var curMonth = 1

    fun scrollMonth(date: YearMonth) {
        curYear = date.year
        curMonth = date.monthValue
        val stringYearMonth = curYear.toString() + "년 " + curMonth + "월"
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

    fun showYearMonthDatePicker() {
        viewModelScope.launch {
            _events.emit(
                CertificationListEvents.ShowYearMonthPicker(
                    curYear = curYear,
                    curMonth = curMonth
                )
            )
        }
    }


    private fun getCurDateCertification(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                certificationList = listOf(
                    CertificationListItem(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        nick = "킹노아",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                    CertificationListItem(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        nick = "킹노아",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                    CertificationListItem(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        nick = "킹노아",
                        certificationImg = "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        certificationCount = "[14회차 인증]",
                        date = "2023년 8월 17일",
                        description = "치킨도 줍고 치킨도 주웠다"
                    ),
                    CertificationListItem(
                        "https://greengrim-bucket.s3.ap-northeast-2.amazonaws.com/19836b51-2b6f-4e43-9e90-e86e331e9077.jpg",
                        nick = "킹노아",
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