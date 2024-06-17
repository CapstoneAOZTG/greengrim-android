package com.aoztg.greengrim.presentation.ui.home.issue

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.data.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentIssueListViewModel @Inject constructor(
    private val repository: MemberRepository
): ViewModel() {

}