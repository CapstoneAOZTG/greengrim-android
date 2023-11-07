package com.aoztg.greengrim.presentation.ui.global.certificationdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCertificationDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class CertificationDetailFragment: BaseFragment<FragmentCertificationDetailBinding>(R.layout.fragment_certification_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: CertificationDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
    }
}