package com.aoztg.greengrim.presentation.ui.info.mycertification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.model.ErrorResponse
import com.aoztg.greengrim.data.repository.CertificationRepository
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.DateState
import com.aoztg.greengrim.presentation.ui.MonthState
import com.aoztg.greengrim.presentation.ui.info.mapper.toMyCertificationListData
import com.aoztg.greengrim.presentation.ui.info.model.MyCertification
import com.aoztg.greengrim.presentation.util.toHeaderText
import com.aoztg.greengrim.presentation.util.toLocalDate
import com.google.gson.Gson
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
    val certificationDateList: List<LocalDate> = emptyList(),
    val certificationList: List<MyCertification> = emptyList(),
    val page: Int = 0,
    val hasNext: Boolean = true,
    val getCertificationListState: BaseState = BaseState.Empty,
    val getCertificationDatesState: BaseState = BaseState.Empty
)

sealed class MyCertificationEvents {
    data class ShowYearMonthPicker(val curYear: Int, val curMonth: Int) : MyCertificationEvents()
}

@HiltViewModel
class MyCertificationViewModel @Inject constructor(
    private val certificationRepository: CertificationRepository
) : ViewModel() {

    companion object{
        const val NEXT_PAGE = 0
        const val NEW_DATE = 1
    }

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
                curDate = DateState.Changed(date.toHeaderText(), date),
                hasNext = true,
                page = 0
            )
        }

        getCertificationList(NEW_DATE)
    }

    private fun getCertificationDate(yearMonth: String) {
        viewModelScope.launch {
            val response = certificationRepository.getMyCertificationDate(yearMonth)

            if (response.isSuccessful) {
                response.body()?.let { data ->
                    _uiState.update { state ->
                        state.copy(
                            certificationDateList = data.date.map { it.toLocalDate() }
                        )
                    }
                }
            } else {
                val error =
                    Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)

                _uiState.update { state ->
                    state.copy(
                        getCertificationDatesState = BaseState.Error(error.message)
                    )
                }
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

    fun getCertificationList(option: Int) {

        if(_uiState.value.hasNext){
            viewModelScope.launch {


                _uiState.update { state ->
                    state.copy(
                        page = state.page
                    )
                }

                val response = certificationRepository.getMyCertificationList(
                    (_uiState.value.curDate as DateState.Changed).originDate.toString(),
                    _uiState.value.page,
                    20
                )

                if(response.isSuccessful){
                    response.body()?.let{ body ->
                        val uiData = body.toMyCertificationListData()
                        _uiState.update { state ->
                            state.copy(
                                certificationList = if(option == NEXT_PAGE) _uiState.value.certificationList + uiData.result else uiData.result,
                                hasNext = uiData.hasNext,
                                page = uiData.page + 1,
                                getCertificationListState = BaseState.Success
                            )
                        }
                    }
                } else{
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)

                    _uiState.update { state ->
                        state.copy(
                            getCertificationListState = BaseState.Error(error.message)
                        )
                    }

                }
            }
        }
    }

}