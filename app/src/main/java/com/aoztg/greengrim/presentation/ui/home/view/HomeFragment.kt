package com.aoztg.greengrim.presentation.ui.home.view

import android.os.Bundle
import android.view.View
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHomeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutToolbar
    }
}