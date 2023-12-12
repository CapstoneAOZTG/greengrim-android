package com.aoztg.greengrim.presentation.ui.info.mykeyword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyKeywordBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyKeywordFragment : BaseFragment<FragmentMyKeywordBinding>(R.layout.fragment_my_keyword) {

    private val viewModel: MyKeywordViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
        viewModel.getMyKeywords()
        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MyKeywordEvents.NavigateToBack -> findNavController().navigateUp()
                    is MyKeywordEvents.ShowSnackMessage -> showCustomSnack(
                        binding.nounDivider,
                        it.msg
                    )
                }
            }
        }
    }
}