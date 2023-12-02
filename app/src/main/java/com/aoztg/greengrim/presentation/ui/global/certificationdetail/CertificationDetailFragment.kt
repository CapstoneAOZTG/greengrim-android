package com.aoztg.greengrim.presentation.ui.global.certificationdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCertificationDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.VerifySnackBar
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CertificationDetailFragment: BaseFragment<FragmentCertificationDetailBinding>(R.layout.fragment_certification_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CertificationDetailViewModel by viewModels()

    private val args: CertificationDetailFragmentArgs by navArgs()
    private val certificationId by lazy { args.certificationId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setCertificationId(certificationId)
        initEventObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCertificationDetail()
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is CertificationDetailEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is CertificationDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is CertificationDetailEvents.ShowVerifySnackBar -> VerifySnackBar.make(binding.tvDescription).show()
                    is CertificationDetailEvents.ShowSnackMessage -> showSnackBar(it.msg)
                    is CertificationDetailEvents.ShowLoading -> showLoading(requireContext())
                    is CertificationDetailEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }
}



