package com.aoztg.greengrim.presentation.ui.info.mycertification

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.CalendarDayLayoutBinding
import com.aoztg.greengrim.databinding.CalendarHeaderBinding
import com.aoztg.greengrim.databinding.FragmentMyCertificationBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.util.toLocalDate
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MyCertificationFragment :
    BaseFragment<FragmentMyCertificationBinding>(R.layout.fragment_my_certification) {

    private var selectedDate: LocalDate? = null
    private var hasEventDate: MutableList<LocalDate> = mutableListOf("2023-10-20".toLocalDate())
    private val today = LocalDate.now()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.calendarView) {

            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(100)
            val endMonth = currentMonth.plusMonths(100)
            val firstDayOfWeek = firstDayOfWeekFromLocale()
            val daysOfWeek = daysOfWeek()

            configureBinders(daysOfWeek)

            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)

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

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)

            @SuppressLint("ResourceAsColor")
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val dateTv = container.textView

                dateTv.text = data.date.dayOfMonth.toString()

                if (data.position != DayPosition.MonthDate) {
                    dateTv.visibility = View.GONE
                }

                when (data.date) {
                    today -> {
                        dateTv.setTextColor(Color.GRAY)
                    }

                    selectedDate -> {
                        dateTv.setBackgroundResource(R.drawable.shape_calendar_selected)
                        dateTv.setTextColor(Color.BLACK)
                    }

                    in hasEventDate -> {
                        dateTv.setBackgroundResource(R.drawable.shape_calendar_hasevent)
                        dateTv.setTextColor(Color.WHITE)
                    }

                    else -> {
                        dateTv.background = null
                        dateTv.setTextColor(Color.WHITE)
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val layout = CalendarHeaderBinding.bind(view).legendLayout.root
        }

        binding.calendarView.monthHeaderBinder =
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
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)
        }
    }


}