package com.aoztg.greengrim.presentation.ui.chat.certificationlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCertificationListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.DateState
import com.aoztg.greengrim.presentation.ui.MonthState
import com.aoztg.greengrim.presentation.ui.chat.adapter.CertificationListAdapter
import com.aoztg.greengrim.presentation.ui.info.mycertification.MyCertificationTempDate
import com.aoztg.greengrim.presentation.ui.info.mycertification.MyCertificationViewModel
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.util.CustomCalendar
import com.kizitonwose.calendar.core.yearMonth
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth

@AndroidEntryPoint
class CertificationListFragment : BaseFragment<FragmentCertificationListBinding>(R.layout.fragment_certification_list) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CertificationListViewModel by viewModels()

    private val args: CertificationListFragmentArgs by navArgs()
    private val challengeId by lazy { args.challengeId }

    private lateinit var customCalendar: CustomCalendar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setChallengeId(challengeId)
        initStateObserver()
        initEventsObserver()
        setScrollEventListener()
        initCustomCalendar()
        setBtnClickListener()
        binding.rvCertifications.adapter = CertificationListAdapter()
        viewModel.getCertificationDate()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.curDate) {
                    is DateState.Changed -> binding.tvDate.text = it.curDate.stringDate
                    else -> {}
                }

                when (it.curMonth) {
                    is MonthState.Changed -> binding.btnSelectMonth.text = it.curMonth.stringMonth
                    else -> {}
                }
            }
        }
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is CertificationListEvents.ShowYearMonthPicker -> {
                        showYearMonthDialog(
                            requireContext(),
                            it.curYear,
                            it.curMonth,
                            ::yearMonthDatePickerConfirmListener
                        )
                    }
                    is CertificationListEvents.NavigateToCertificationDetail -> {
                        CertificationListTempDate.setTempDate(customCalendar.selectedDate)
                        findNavController().toCertificationDetail(it.certificationId)
                    }
                    is CertificationListEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is CertificationListEvents.ShowCalendar -> customCalendar.setDateWithDataList(
                        viewModel.uiState.value.certificationDateList
                    )
                    is CertificationListEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun setScrollEventListener() {

        // NestedScrollView 안에 RecyclerView 삽입하여서, 마지막 아이템 감지로 페이징하면 안됨
        // 또한, 최하단 스크롤 감지를, recyclerview 가 아니라 NestedScrollView 로 해야함


        binding.scrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                viewModel.getCertificationList(MyCertificationViewModel.NEXT_PAGE)
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

    private fun initCustomCalendar(){
        val tempDate = CertificationListTempDate.getTempDate()
        customCalendar = CustomCalendar(
            binding.calendarView,
            ::monthScrollListener,
            ::dateSelectListener,
            selectedMonth = tempDate.yearMonth,
            selectedDate = tempDate
        )
    }

    private fun setBtnClickListener(){
        binding.btnNextMonth.setOnClickListener{
            customCalendar.goToNextMonth()
        }

        binding.btnPreviousMonth.setOnClickListener{
            customCalendar.goToPreviousMonth()
        }
    }



    private fun monthScrollListener(data: YearMonth){
        viewModel.scrollMonth(data)
    }

    private fun dateSelectListener(data: LocalDate){
        viewModel.selectDate(data)
    }

    private fun yearMonthDatePickerConfirmListener(year:Int, month:Int){
        customCalendar.yearMonthDatePickerConfirmListener(year, month)
    }


}