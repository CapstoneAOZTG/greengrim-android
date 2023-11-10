package com.aoztg.greengrim.presentation.ui.chat.createcertification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateCertificationBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCertificationFragment: BaseFragment<FragmentCreateCertificationBinding>(R.layout.fragment_create_certification) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CreateCertificationViewModel by viewModels()

    private val args: CreateCertificationFragmentArgs by navArgs()
    private val challengeId by lazy { args.challengeId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pvm = parentViewModel
        binding.vm = viewModel
        initImgObserver()
        initEventObserver()
    }

    private fun initImgObserver(){
        repeatOnStarted {
            parentViewModel.image.collect {
                if (it.isNotBlank()) {
                    viewModel.setImgUrl(it)
                }
            }
        }
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is CreateCertificationEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }


}