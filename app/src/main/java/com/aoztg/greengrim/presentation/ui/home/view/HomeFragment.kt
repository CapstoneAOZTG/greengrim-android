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
import com.aoztg.greengrim.presentation.util.ViewPagerUtil

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){


    private val viewModel : HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        setData()
    }

    private fun setObserver(){
        viewModel.hotChallengeList.observe(viewLifecycleOwner){
            val homeAdapter = HomeAdapter(it, vpItemType.HOT_CHALLENGE)
            with(binding.vpHotChallenge){
                adapter = homeAdapter
                offscreenPageLimit = 3
                setPageTransformer(ViewPagerUtil.getTransform())
            }
        }

        viewModel.moreActivityList.observe(viewLifecycleOwner){
            val homeAdapter = HomeAdapter(it, vpItemType.MORE_ACTIVITY)
            with(binding.vpMoreActivity){
                adapter = homeAdapter
                offscreenPageLimit = 3
                setPageTransformer(ViewPagerUtil.getTransform())
            }
        }

        viewModel.hotNftList.observe(viewLifecycleOwner){
            val homeAdapter = HomeAdapter(it, vpItemType.HOT_NFT)
            with(binding.vpHotNft){
                adapter = homeAdapter
                offscreenPageLimit = 3
                setPageTransformer(ViewPagerUtil.getTransform())
            }
        }

    }

    private fun setData(){
        viewModel.getHomeList()
    }
}