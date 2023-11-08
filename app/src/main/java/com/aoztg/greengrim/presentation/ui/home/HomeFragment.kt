package com.aoztg.greengrim.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHomeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.LoadingState
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
        initStateObserver()
        viewModel.getHomeList()
    }

    private fun initRecycler() {
        repeatOnStarted {
            viewModel.hotChallengeList.collect {
                if (it.isNotEmpty()) {
                    binding.rvHotChallenge.adapter = HotChallengeAdapter(it)
                    recyclerToViewPager(binding.rvHotChallenge, binding.indicatorHotChallenge)
                }
            }
        }

        repeatOnStarted {
            viewModel.hotNftList.collect {
                if (it.isNotEmpty()) {
                    binding.rvHotNft.adapter = HotNftAdapter(it)
                    recyclerToViewPager(binding.rvHotNft, binding.indicatorHotNft)
                }
            }
        }

        repeatOnStarted {
            viewModel.moreActivityList.collect {
                if (it.isNotEmpty()) {
                    binding.rvMoreActivity.adapter = MoreActivityAdapter(it)
                    recyclerToViewPager(binding.rvMoreActivity, binding.indicatorMoreActivity)
                }
            }
        }
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.loading) {
                    is LoadingState.IsLoading -> {
                        if (it.loading.state) showLoading(requireContext())
                        else dismissLoading()
                    }

                    else -> {}
                }
            }
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