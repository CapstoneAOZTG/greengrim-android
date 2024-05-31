package com.aoztg.greengrim.presentation.ui.global.adapter

interface AlarmClickListener {
    fun navigateToCertificationDetail(id: Long)
    fun navigateToChallengeDetail(id: Long)
    fun navigateToNftDetail(id: Long)
    fun navigateToIssueDetail(id: Long)
}