package com.aoztg.greengrim.presentation.ui.home.issue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentRecentIssueListBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecentIssueListFragment: BaseFragment<FragmentRecentIssueListBinding>(R.layout.fragment_recent_issue_list) {

    private val viewModel: RecentIssueListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}