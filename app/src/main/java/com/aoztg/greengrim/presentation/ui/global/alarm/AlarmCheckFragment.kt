package com.aoztg.greengrim.presentation.ui.global.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAlarmCheckBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.adapter.AlarmAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmCheckFragment: BaseFragment<FragmentAlarmCheckBinding>(R.layout.fragment_alarm_check) {


    private val viewModel : AlarmCheckViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAlarmList.adapter = AlarmAdapter()
        binding.vm = viewModel
        viewModel.getAlarmList()
    }


}