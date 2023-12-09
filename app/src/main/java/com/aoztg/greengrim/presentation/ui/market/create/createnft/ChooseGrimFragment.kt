package com.aoztg.greengrim.presentation.ui.market.create.createnft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChooseGrimBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.market.adapter.ChooseGrimAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGrimFragment: BaseFragment<FragmentChooseGrimBinding>(R.layout.fragment_choose_grim) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChooseGrimViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvMyGrim.adapter = ChooseGrimAdapter()
        viewModel.getMyGrimForNft()
        initEventObserver()
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is ChooseGrimEvent.NavigateToBack -> findNavController().navigateUp()
                    is ChooseGrimEvent.NavigateToCreatePaint -> findNavController().toCreatePaint()
                    is ChooseGrimEvent.NavigateToCreateNft -> findNavController().toCreateNft(it.grimId)
                    is ChooseGrimEvent.ShowSnackMessage -> showCustomSnack(binding.rvMyGrim, it.msg)
                }
            }
        }
    }

    private fun NavController.toCreateNft(grimId: Int){
        val action = ChooseGrimFragmentDirections.actionChooseGrimFragmentToCreateNftFragment(grimId)
        navigate(action)
    }

    private fun NavController.toCreatePaint(){
        val action = ChooseGrimFragmentDirections.actionChooseGrimFragmentToCreatePaintFragment()
        navigate(action)
    }
}