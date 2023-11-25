package com.aoztg.greengrim.presentation.ui.info.mychallenge

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyChallengeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.BaseState
import com.aoztg.greengrim.presentation.ui.LoadingState
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.info.mychallenge.MyChallengeViewModel.Companion.ORIGINAL
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.customview.getSortSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyChallengeFragment :
    BaseFragment<FragmentMyChallengeBinding>(R.layout.fragment_my_challenge) {

    private val viewModel: MyChallengeViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var sortType = ChallengeSortType.DESC
    private var loadingState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.vm = viewModel
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        viewModel.getMyChallenge(ORIGINAL)
        initStateObserver()
        initEventObserver()
        setScrollEventListener()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {

                when (it.getChallengeRoomState) {
                    is BaseState.Error -> showCustomToast(it.getChallengeRoomState.msg)
                    else -> {}
                }

                when (it.loading) {
                    is LoadingState.IsLoading -> {
                        if (it.loading.state) {
                            if (!loadingState) {
                                showLoading(requireContext())
                                loadingState = true
                            }
                        } else {
                            dismissLoading()
                            loadingState = false
                        }
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
                    is MyChallengeEvents.NavigateToChallengeDetail -> findNavController().toChallengeDetail(
                        it.id
                    )

                    is MyChallengeEvents.ShowBottomSheet -> showBottomSheet()
                    is MyChallengeEvents.ScrollToTop -> binding.rvChallengeList.smoothScrollToPosition(0)
                    else -> {}
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvChallengeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getMyChallenge(ORIGINAL)
                }
            }
        })
    }

    private fun showBottomSheet() {
        getSortSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setSortType(type)
            binding.tvFilter.text = type.text
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (loadingState) dismissLoading()
    }
}