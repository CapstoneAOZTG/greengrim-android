package com.aoztg.greengrim.presentation.ui.mypage.myprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyProfileBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.ChallengeFilterBottomSheet
import com.aoztg.greengrim.presentation.ui.challenge.adapter.ChallengeRoomAdapter
import com.aoztg.greengrim.presentation.ui.challenge.list.SortType
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.mypage.mycertification.MyCertificationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(R.layout.fragment_my_profile) {

    companion object {
        const val SORT = 0
        const val ORIGINAL = 1
    }

    private val parentViewModel : MainViewModel by activityViewModels()
    private val viewModel : MyProfileViewModel by viewModels()
    private var sortType = SortType.DESC

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvChallengeList.adapter = ChallengeRoomAdapter()
        setScrollEventListener()
    }

    private fun setScrollEventListener() {

        binding.scrollView.setOnScrollChangeListener { v, _, _, _, _ ->
            if (!v.canScrollVertically(1)) {
                when(viewModel.uiState.value.curFilter){
                    CurProfileFilter.CHALLENGE -> {
                        viewModel.getMyChallenge(ORIGINAL)
                    }

                    CurProfileFilter.CERTIFICATION -> {

                    }

                    CurProfileFilter.NFT -> {

                    }
                }
            }
        }
    }

    private fun showChallengeFilterBottomSheet() {
        ChallengeFilterBottomSheet(requireContext(), sortType) { type ->
            sortType = type
            viewModel.setChallengeSortType(type)
            binding.tvChallengeFilter.text = type.text
        }.show()
    }
}