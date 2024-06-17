package com.aoztg.greengrim.presentation.ui.mypage.announce

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAnnounceListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.mypage.adapter.AnnounceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnounceListFragment: BaseFragment<FragmentAnnounceListBinding>(R.layout.fragment_announce_list) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: AnnounceListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvAnnounceList.adapter = AnnounceAdapter()
        viewModel.getAnnounceListData()
        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is AnnounceListEvent.NavigateToAnnounceDetail -> findNavController().toAnnounceDetail(it.id)
                    is AnnounceListEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun NavController.toAnnounceDetail(id: Long){
        val action = AnnounceListFragmentDirections.actionAnnounceListFragmentToAnnounceDetailFragment(id)
        navigate(action)
    }
}