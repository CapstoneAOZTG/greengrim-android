package com.aoztg.greengrim.presentation.ui.global.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAlarmCheckBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.adapter.AlarmAdapter
import com.aoztg.greengrim.presentation.ui.global.adapter.AlarmClickListener
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmCheckFragment : BaseFragment<FragmentAlarmCheckBinding>(R.layout.fragment_alarm_check),
    AlarmClickListener {


    private val viewModel: AlarmCheckViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAlarmList.adapter = AlarmAdapter()
        binding.vm = viewModel
        viewModel.getAlarmList()
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is AlarmCheckEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    override fun navigateToCertificationDetail(id: Long) {
        findNavController().toCertificationDetail(id)
    }

    override fun navigateToChallengeDetail(id: Long) {
        findNavController().toChallengeDetail(id)
    }

    override fun navigateToIssueDetail(id: Long) {
        // todo issue detail 로 이동
    }

    override fun navigateToNftDetail(id: Long) {
        findNavController().toNftDetail(id)
    }

}