package com.aoztg.greengrim.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHomeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.home.adapter.HotChallengeAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.HotNftAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.MoreActivityAdapter
import me.relex.circleindicator.CircleIndicator2


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        initRecycler()
        viewModel.getHomeList()
    }

    private fun initRecycler() {
        with(binding) {
            rvHotChallenge.adapter = HotChallengeAdapter()
            rvHotNft.adapter = HotNftAdapter()
            rvMoreActivity.adapter = MoreActivityAdapter()
            recyclerToViewPager(rvHotChallenge, indicatorHotChallenge)
            recyclerToViewPager(rvHotNft, indicatorHotNft)
            recyclerToViewPager(rvMoreActivity, indicatorMoreActivity)
        }
    }

    private fun recyclerToViewPager(
        recycler: RecyclerView,
        indicator: CircleIndicator2
    ) {

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recycler)

        indicator.attachToRecyclerView(recycler, pagerSnapHelper)
    }
}