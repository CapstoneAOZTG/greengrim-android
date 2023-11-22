package com.aoztg.greengrim.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentHomeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.home.adapter.HotChallengeAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.HotNftAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.MoreActivityAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator2


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    private var isHotChallengeSet: Boolean = false
    private var isMoreActivitySet: Boolean = false
    private var isHotNftSet: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        initRecycler()
        initStateObserver()
        initEventObserver()
        viewModel.getHomeData()
    }

    private fun initRecycler() {
        repeatOnStarted {
            viewModel.uiState.collect{
                if(it.hotChallengeList.isNotEmpty() && !isHotChallengeSet){
                    binding.rvHotChallenge.adapter = HotChallengeAdapter(it.hotChallengeList)
                    recyclerToViewPager(binding.rvHotChallenge, binding.indicatorHotChallenge)
                    isHotChallengeSet = true
                }

                if(it.moreActivityList.isNotEmpty() && !isMoreActivitySet){
                    binding.rvMoreActivity.adapter = MoreActivityAdapter(it.moreActivityList)
                    recyclerToViewPager(binding.rvMoreActivity, binding.indicatorMoreActivity)
                    isMoreActivitySet = true
                }

                if(it.hotNftList.isNotEmpty() && !isHotNftSet){
                    binding.rvHotNft.adapter = HotNftAdapter(it.hotNftList)
                    recyclerToViewPager(binding.rvHotNft, binding.indicatorHotNft)
                    isHotNftSet = true
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

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is HomeEvents.NavigateToChallengeDetail -> findNavController().toChallengeDetail(
                        it.id
                    )
                    is HomeEvents.ShowToastMessage -> showCustomToast(it.msg)
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