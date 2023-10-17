package com.aoztg.greengrim.presentation.ui.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHomeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.home.adapter.HomeAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.vpItemType
import com.aoztg.greengrim.presentation.ui.home.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){


    private val viewModel : HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        setData()
    }

    private fun setObserver(){
//        viewModel.hotChallengeList.observe(viewLifecycleOwner){
//            val adapter = HomeAdapter(it, vpItemType.HOT_CHALLENGE)
//            binding.vpHotChallenge.adapter = adapter
//        }
//
        viewModel.moreActivityList.observe(viewLifecycleOwner){
            val adapter = HomeAdapter(it, vpItemType.MORE_ACTIVITY)
            binding.vpMoreActivity.adapter = adapter
        }
//
//        viewModel.hotNftList.observe(viewLifecycleOwner){
//            val adapter = HomeAdapter(it, vpItemType.HOT_NFT)
//            binding.vpHotNft.adapter = adapter
//        }
    }

    private fun setData(){
        viewModel.getHomeList()
    }
}