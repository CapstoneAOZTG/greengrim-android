package com.aoztg.greengrim.presentation.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.CalendarDayLayoutBinding
import com.aoztg.greengrim.databinding.CalendarHeaderBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class CustomCalendar(
    private val calendarView: CalendarView,
    private val monthScrollListener: (YearMonth) -> Unit,
    private val dateSelectListener: (LocalDate) -> Unit
) {

    private var selectedDate: LocalDate? = null
    private var currentMonth: YearMonth = YearMonth.now()
    private var dataWithDateList = listOf<LocalDate>()
    private val today = LocalDate.now()

    init{
        initCalenderView()
    }

    @SuppressLint("SetTextI18n")
    private fun initCalenderView() {
        with(calendarView) {

            val startMonth = currentMonth.minusMonths(100)
            val endMonth = currentMonth.plusMonths(100)
            val firstDayOfWeek = firstDayOfWeekFromLocale()
            val daysOfWeek = daysOfWeek()

            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)

            monthScrollListener = {
                selectDate(it.yearMonth.atDay(1))
                monthScrollListener(it.yearMonth)
            }

            configureBinders(daysOfWeek)
        }
    }

    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        selectDate(day.date)
                    }
                }
            }
        }

        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View): DayViewContainer = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val dateTv = container.textView

                dateTv.text = data.date.dayOfMonth.toString()
                if (data.position != DayPosition.MonthDate) {
                    dateTv.visibility = View.GONE
                } else {
                    dateTv.visibility = View.VISIBLE
                    when (data.date) {
                        selectedDate -> {
                            dateTv.setBackgroundResource(R.drawable.shape_calendar_selected)
                            dateTv.setTextColor(Color.BLACK)
                        }

                        in dataWithDateList -> {
                            dateTv.setBackgroundResource(R.drawable.shape_calendar_hasevent)
                            dateTv.setTextColor(Color.WHITE)
                        }

                        today -> {
                            dateTv.background = null
                            dateTv.setTextColor(Color.GRAY)
                        }

                        else -> {
                            dateTv.background = null
                            dateTv.setTextColor(Color.WHITE)
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val layout = CalendarHeaderBinding.bind(view).legendLayout.root
        }

        calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    if (container.layout.tag == null) {
                        container.layout.tag = data.yearMonth
                        container.layout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].name.first().toString()
                            }
                    }
                }
            }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            oldDate?.let { calendarView.notifyDateChanged(it) }
            calendarView.notifyDateChanged(date)
            selectedDate = date
            dateSelectListener(date)
        }
    }

    fun setDateWithDataList(dateList: List<LocalDate>){
        dataWithDateList = dateList
        dateList.forEach{
            calendarView.notifyDateChanged(it)
        }
    }

    fun goToNextMonth(){
        currentMonth = currentMonth.plusMonths(1)
        calendarView.smoothScrollToMonth(currentMonth)
    }

    fun goToPreviousMonth(){
        currentMonth = currentMonth.minusMonths(1)
        calendarView.smoothScrollToMonth(currentMonth)
    }

    fun yearMonthDatePickerConfirmListener(year: Int, month: Int) {
        currentMonth = YearMonth.of(year, month)
        calendarView.scrollToMonth(currentMonth)
    }
}