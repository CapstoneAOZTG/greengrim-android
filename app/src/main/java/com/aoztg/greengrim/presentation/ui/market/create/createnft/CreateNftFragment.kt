package com.aoztg.greengrim.presentation.ui.market.create.createnft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNftFragment: BaseFragment<FragmentCreateNftBinding>(R.layout.fragment_create_nft) {

    private val viewModel: CreateNftViewModel by viewModels()
    private val args: CreateNftFragmentArgs by navArgs()
    private val grimId by lazy { args.grimId }
    private val grimUrl by lazy { args.grimUrl }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}