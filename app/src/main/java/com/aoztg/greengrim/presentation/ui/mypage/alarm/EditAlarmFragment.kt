package com.aoztg.greengrim.presentation.ui.mypage.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentEditAlarmBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAlarmFragment : BaseFragment<FragmentEditAlarmBinding>(R.layout.fragment_edit_alarm) {

    private val viewModel: EditAlarmViewModel by viewModels()
    private val parentViewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
        initStateObserve()
    }

    private fun initStateObserve(){
        repeatOnStarted {
            viewModel.pushAlarmState.collect{
                if(it){
                    binding.switchPushAlarm.trackTintList =
                        requireContext().resources.getColorStateList(
                            R.color.gg_green,
                            requireContext().theme
                        )
                } else {
                    binding.switchPushAlarm.trackTintList =
                        requireContext().resources.getColorStateList(
                            R.color.gg_grey2,
                            requireContext().theme
                        )
                }
            }
        }

        repeatOnStarted {
            viewModel.advertisingState.collect{
                if(it){
                    binding.switchAdvertising.trackTintList =
                        requireContext().resources.getColorStateList(
                            R.color.gg_green,
                            requireContext().theme
                        )
                } else {
                    binding.switchAdvertising.trackTintList =
                        requireContext().resources.getColorStateList(
                            R.color.gg_grey2,
                            requireContext().theme
                        )
                }
            }
        }
    }
}