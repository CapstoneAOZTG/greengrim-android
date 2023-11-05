package com.aoztg.greengrim.presentation.ui.global.attendcheck

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAttendCheckBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class AttendCheckFragment : BaseFragment<FragmentAttendCheckBinding>(R.layout.fragment_attend_check) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: AttendCheckViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
    }
}