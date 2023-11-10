package com.aoztg.greengrim.presentation.ui.chat.createcertification

import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateCertificationBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCertificationFragment: BaseFragment<FragmentCreateCertificationBinding>(R.layout.fragment_create_certification) {

    private val args: CreateCertificationFragmentArgs by navArgs()
    private val challengeId by lazy { args.challengeId }


}