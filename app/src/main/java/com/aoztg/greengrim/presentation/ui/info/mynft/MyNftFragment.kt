package com.aoztg.greengrim.presentation.ui.info.mynft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class MyNftFragment: BaseFragment<FragmentMyNftBinding>(R.layout.fragment_my_nft) {

    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
    }
}