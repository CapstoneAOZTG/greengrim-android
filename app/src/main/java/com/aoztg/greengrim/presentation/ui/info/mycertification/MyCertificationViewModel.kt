package com.aoztg.greengrim.presentation.ui.info.mycertification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.presentation.ui.DateState
import com.aoztg.greengrim.presentation.ui.MonthState
import com.aoztg.greengrim.presentation.ui.info.model.MyCertification
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

data class MyCertificationUiState(
    val curMonth: MonthState = MonthState.Empty,
    val curDate: DateState = DateState.Empty,
    val eventDateList: List<LocalDate> = emptyList(),
    val certificationList: List<MyCertification> = emptyList()
)

sealed class MyCertificationEvents {
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : MyCertificationEvents()
}

@HiltViewModel
class MyCertificationViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyCertificationUiState())
    val uiState: StateFlow<MyCertificationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MyCertificationEvents>()
    val events: SharedFlow<MyCertificationEvents> = _events.asSharedFlow()

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
        getCertificationDate(curYear.toString() + "-" + if (curMonth < 10) "0$curMonth" else curMonth.toString())
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                curDate = DateState.Changed(date.toHeaderText(), date)
            )
        }

        getCurDateCertification(date)
    }

    private fun getCertificationDate(yearMonth: String) {
        viewModelScope.launch {
            val response = certificationRepository.getMyCertificationDate(yearMonth)

            if (response.isSuccessful) {
                response.body()?.let { data ->
                    _uiState.update { state ->
                        state.copy(
                            eventDateList = data.date.map { it.toLocalDate() }
                        )
                    }
                }
            } else {

            }
        }

    }

    fun showYearMonthDatePicker() {
        viewModelScope.launch {
            _events.emit(
                MyCertificationEvents.ShowYearMonthPicker(
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