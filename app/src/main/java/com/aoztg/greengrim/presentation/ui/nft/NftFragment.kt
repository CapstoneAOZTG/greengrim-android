package com.aoztg.greengrim.presentation.ui.nft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.NftFilterBottomSheet
import com.aoztg.greengrim.presentation.customview.NftSortType
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.nft.adapter.NftItemAdapter
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftFragment : BaseFragment<FragmentNftBinding>(R.layout.fragment_nft) {

    companion object {
        const val NEW = 0
        const val NEXT_PAGE = 1
    }

    private val viewModel: MarketViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var isHotNftSet: Boolean = false
    private var sortType = NftSortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvGreenNftList.adapter = NftItemAdapter()
        initEventObserver()
        setScrollEventListener()
        viewModel.getNftList(NEW)
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

        binding.scrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                viewModel.getNftList(NEXT_PAGE)
            }
        }
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