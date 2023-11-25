package com.aoztg.greengrim.presentation.ui.info.mycertification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.MainNavDirections
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyCertificationBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.DateState
import com.aoztg.greengrim.presentation.ui.MonthState
import com.aoztg.greengrim.presentation.ui.info.adapter.MyCertificationAdapter
import com.aoztg.greengrim.presentation.ui.info.mycertification.MyCertificationViewModel.Companion.NEXT_PAGE
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.util.CustomCalendar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth

@AndroidEntryPoint
class MyCertificationFragment :
    BaseFragment<FragmentMyCertificationBinding>(R.layout.fragment_my_certification) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyCertificationViewModel by viewModels()

    private var selectedDate: LocalDate? = null
    private lateinit var customCalendar: CustomCalendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        initCustomCalendar()
        initStateObserver()
        initEventsObserver()
        setScrollEventListener()
        setBtnClickListener()
        binding.rvCertifications.adapter = MyCertificationAdapter()
        viewModel.getCertificationDate()
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

                when (it.getCertificationListState) {
                    is BaseState.Error -> showCustomToast(it.getCertificationListState.msg)
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

                    is MyCertificationEvents.NavigateToCertificationDetail -> findNavController().toCertificationDetail(
                        it.certificationId
                    )
                    is MyCertificationEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is MyCertificationEvents.ShowCalendar -> customCalendar.setDateWithDataList(
                        viewModel.uiState.value.certificationDateList
                    )
                    is MyCertificationEvents.NavigateToBack -> findNavController().navigateUp()
                    else -> {}
                }
            }
        }
    }

    private fun yearMonthDatePickerConfirmListener(year:Int, month:Int){
        customCalendar.yearMonthDatePickerConfirmListener(year, month)
    }

    private fun setScrollEventListener() {

        // NestedScrollView 안에 RecyclerView 삽입하여서, 마지막 아이템 감지로 페이징하면 안됨
        // 또한, 최하단 스크롤 감지를, recyclerview 가 아니라 NestedScrollView 로 해야함


        binding.scrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                viewModel.getCertificationList(NEXT_PAGE)
            }
        }

//        binding.rvCertifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
//                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)
//
//                if (lastVisibleItemPosition == itemTotalCount) {
//                    viewModel.getCertificationList(NEXT_PAGE)
//                }
//            }
//        })
    }

    private fun setBtnClickListener(){
        binding.btnNextMonth.setOnClickListener{
            customCalendar.goToNextMonth()
        }

        binding.btnPreviousMonth.setOnClickListener{
            customCalendar.goToPreviousMonth()
        }
    }

    private fun initCustomCalendar(){
        customCalendar = CustomCalendar(
            binding.calendarView,
            ::monthScrollListener,
            ::dateSelectListener,
        )
    }

    private fun monthScrollListener(data: YearMonth){
        viewModel.scrollMonth(data)
    }

    private fun dateSelectListener(data: LocalDate){
        viewModel.selectDate(data)
    }

    private fun NavController.toCertificationDetail(certificationId: Int) {
        val action = MainNavDirections.actionGlobalToCertificationDetail(certificationId)
        this.navigate(action)
    }
}