package com.aoztg.greengrim.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHomeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.home.adapter.HomeAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.vpItemType
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()
        setData()
    }

    private fun setObserver() {
        viewModel.hotChallengeList.observe(viewLifecycleOwner) {
            setRecycler(it, vpItemType.HOT_CHALLENGE, binding.rvHotChallenge)
        }

        viewModel.moreActivityList.observe(viewLifecycleOwner) {
            setRecycler(it, vpItemType.MORE_ACTIVITY, binding.rvMoreActivity)
        }

        viewModel.hotNftList.observe(viewLifecycleOwner) {
            setRecycler(it, vpItemType.HOT_NFT, binding.rvHotNft)
        }
    }

    private fun setRecycler(
        data: List<HomeUiModel>,
        type: vpItemType,
        rcView: RecyclerView
    ) {
        val adapter = HomeAdapter(data, type)
        rcView.adapter = adapter

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rcView)

        when (type) {
            vpItemType.HOT_CHALLENGE -> binding.indicatorHotChallenge.attachToRecyclerView(
                rcView,
                pagerSnapHelper
            )

            vpItemType.MORE_ACTIVITY -> binding.indicatorMoreActivity.attachToRecyclerView(
                rcView,
                pagerSnapHelper
            )

            vpItemType.HOT_NFT -> binding.indicatorHotNft.attachToRecyclerView(
                rcView,
                pagerSnapHelper
            )
        }
    }

    private fun setData() {
        viewModel.getHomeList()
    }
}