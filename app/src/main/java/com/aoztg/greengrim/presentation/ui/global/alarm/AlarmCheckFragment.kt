package com.aoztg.greengrim.presentation.ui.global.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAlarmCheckBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.adapter.AlarmAdapter
import com.aoztg.greengrim.presentation.ui.global.adapter.AlarmClickListener
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toCertificationDetail
import com.aoztg.greengrim.presentation.ui.toChallengeDetail
import com.aoztg.greengrim.presentation.ui.toNftDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmCheckFragment : BaseFragment<FragmentAlarmCheckBinding>(R.layout.fragment_alarm_check),
    AlarmClickListener {


    private val viewModel: AlarmCheckViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private var adapter: AlarmAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        adapter = AlarmAdapter()
        adapter?.setOnItemClickListener(this)
        binding.rvAlarmList.adapter = adapter
        binding.vm = viewModel
        viewModel.getAlarmList()
        initEventObserve()
        recyclerListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is AlarmCheckEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun recyclerListener() {
        binding.rvAlarmList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getAlarmList()
                }
            }
        })
    }

    override fun navigateToCertificationDetail(id: Long) {
        findNavController().toCertificationDetail(id)
    }

    override fun navigateToChallengeDetail(id: Long) {
        findNavController().toChallengeDetail(id)
    }

    override fun navigateToIssueDetail(id: Long) {
        // todo issue detail 로 이동
    }

    override fun navigateToNftDetail(id: Long) {
        findNavController().toNftDetail(id)
    }

}