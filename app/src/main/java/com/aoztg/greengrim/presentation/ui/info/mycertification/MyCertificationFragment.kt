package com.aoztg.greengrim.presentation.ui.info.mycertification

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.CalendarDayLayoutBinding
import com.aoztg.greengrim.databinding.CalendarHeaderBinding
import com.aoztg.greengrim.databinding.FragmentMyCertificationBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.DateState
import com.aoztg.greengrim.presentation.ui.MonthState
import com.aoztg.greengrim.presentation.ui.info.adapter.MyCertificationAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@AndroidEntryPoint
class MyCertificationFragment :
    BaseFragment<FragmentMyCertificationBinding>(R.layout.fragment_my_certification) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyCertificationViewModel by viewModels()

    private var selectedDate: LocalDate? = null
    private var currentMonth: YearMonth = YearMonth.now()
    private val today = LocalDate.now()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvCertifications.adapter = MyCertificationAdapter()
        initStateObserver()
        initEventsObserver()
        initCalenderView()
    }

    @SuppressLint("SetTextI18n")
    private fun initCalenderView() {
        with(binding.calendarView) {

            val startMonth = currentMonth.minusMonths(100)
            val endMonth = currentMonth.plusMonths(100)
            val firstDayOfWeek = firstDayOfWeekFromLocale()
            val daysOfWeek = daysOfWeek()

            setup(startMonth, endMonth, firstDayOfWeek)
            scrollToMonth(currentMonth)

            monthScrollListener = {
                selectDate(it.yearMonth.atDay(1))
                viewModel.scrollMonth(it.yearMonth)
            }

            binding.btnNextMonth.setOnClickListener {
                currentMonth = currentMonth.plusMonths(1)
                smoothScrollToMonth(currentMonth)
            }

            binding.btnPreviousMonth.setOnClickListener {
                currentMonth = currentMonth.minusMonths(1)
                smoothScrollToMonth(currentMonth)
            }

            configureBinders(daysOfWeek)
        }

    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.curDate) {
                    is DateState.Changed -> {
                        selectedDate = it.curDate.originDate
                        binding.tvDate.text = it.curDate.stringDate
                    }

                    else -> {}
                }

                when (it.curMonth) {
                    is MonthState.Changed -> {
                        binding.btnSelectMonth.text = it.curMonth.stringMonth
                    }

                    else -> {}
                }
            }
        }
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MyCertificationEvents.ShowYearMonthPicker -> {
                        showYearMonthDialog(
                            requireContext(),
                            it.curYear,
                            it.curMonth,
                            ::yearMonthDatePickerConfirmListener
                        )
                    }

                }
            }
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

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val dateTv = container.textView

                dateTv.text = data.date.dayOfMonth.toString()
                if (data.position != DayPosition.MonthDate) {
                    dateTv.visibility = View.GONE
                } else {
                    dateTv.visibility = View.VISIBLE
                    when (data.date) {
                        today -> {
                            dateTv.setTextColor(Color.GRAY)
                        }

                        selectedDate -> {
                            dateTv.setBackgroundResource(R.drawable.shape_calendar_selected)
                            dateTv.setTextColor(Color.BLACK)
                        }

                        in viewModel.uiState.value.eventDateList -> {
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
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)
            viewModel.selectDate(date)
        }
    }

    private fun yearMonthDatePickerConfirmListener(year: Int, month: Int) {
        currentMonth = YearMonth.of(year, month)
        binding.calendarView.scrollToMonth(currentMonth)
    }
}