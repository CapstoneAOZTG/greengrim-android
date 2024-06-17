package com.aoztg.greengrim.presentation.ui.home.issue

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentRecentIssueDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentIssueDetailFragment: BaseFragment<FragmentRecentIssueDetailBinding>(R.layout.fragment_recent_issue_detail) {

    private val viewModel : RecentIssueDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}