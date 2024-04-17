package com.aoztg.greengrim.presentation.ui.nft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.NftFilterBottomSheet
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftFragment : BaseFragment<FragmentNftBinding>(R.layout.fragment_nft) {

    private val viewModel: MarketViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var isHotNftSet: Boolean = false
    private var sortType = NftSortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        initEventObserver()
        setScrollEventListener()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is NftEvent.NavigateToNftDetail -> findNavController().toNftDetail(it.id)
                    is NftEvent.NavigateToNftList -> findNavController().toNftList()
                    is NftEvent.ShowBottomSheet -> showBottomSheet()
                    is NftEvent.ShowLoading -> showLoading(requireContext())
                    is NftEvent.DismissLoading -> dismissLoading()
                    is NftEvent.ShowSnackMessage -> showCustomSnack(binding.tvNftTitle, it.msg)
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvGreenNftList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                }
            }
        })
    }

    private fun showBottomSheet() {
        NftFilterBottomSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }

    private fun NavController.toNftList() {
        val action = NftFragmentDirections.actionNftFragmentToNftListFragment()
        navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isHotNftSet = false
    }
}