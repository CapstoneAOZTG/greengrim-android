package com.aoztg.greengrim.presentation.ui.market

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMarketBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.GrimNftFilterBottomSheet
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.home.adapter.HotChallengeAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.HotNftAdapter
import com.aoztg.greengrim.presentation.ui.home.adapter.MoreActivityAdapter
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.market.MarketViewModel.Companion.ORIGINAL
import com.aoztg.greengrim.presentation.ui.market.adapter.GrimItemAdapter
import com.aoztg.greengrim.presentation.ui.toGrimDetail
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator2

@AndroidEntryPoint
class MarketFragment : BaseFragment<FragmentMarketBinding>(R.layout.fragment_market) {

    private val viewModel: MarketViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var sortType = GrimNftSortType.DESC

    private var isHotNftSet: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvPaintList.adapter = GrimItemAdapter()
        initRecycler()
        initEventObserver()
        setScrollEventListener()
        viewModel.getHotNft()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGrimList(ChallengeListViewModel.ORIGINAL)
    }

    private fun initRecycler() {
        repeatOnStarted {
            viewModel.uiState.collect {
                if(it.hotNftList.isNotEmpty() && !isHotNftSet){
                    binding.rvRecentNft.adapter = HotNftAdapter(it.hotNftList)
                    recyclerToViewPager(binding.rvRecentNft, binding.indicatorRecentNft)
                    isHotNftSet = true
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

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MarketEvents.NavigateToGrimDetail -> findNavController().toGrimDetail(it.id)
                    is MarketEvents.NavigateToNftDetail -> findNavController().toNftDetail(it.id)
                    is MarketEvents.NavigateToNftList -> findNavController().toNftList()
                    is MarketEvents.NavigateToCreateNftOrPaint -> findNavController().toCreateNftOrPaint()
                    is MarketEvents.ShowBottomSheet -> showBottomSheet()
                    is MarketEvents.ScrollToTop -> binding.rvPaintList.smoothScrollToPosition(0)
                    is MarketEvents.ShowLoading -> showLoading(requireContext())
                    is MarketEvents.DismissLoading -> dismissLoading()
                    is MarketEvents.ShowSnackMessage -> showCustomSnack(binding.rvRecentNft, it.msg)
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvPaintList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getGrimList(ORIGINAL)
                }
            }
        })
    }

    private fun showBottomSheet() {
        GrimNftFilterBottomSheet(requireContext(), sortType){ type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }

    private fun NavController.toCreateNftOrPaint() {
        val action = MarketFragmentDirections.actionMarketFragmentToCreateNftOrPaintFragment()
        navigate(action)
    }

    private fun NavController.toNftList() {
        val action = MarketFragmentDirections.actionMarketFragmentToNftListFragment()
        navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isHotNftSet = false
    }
}