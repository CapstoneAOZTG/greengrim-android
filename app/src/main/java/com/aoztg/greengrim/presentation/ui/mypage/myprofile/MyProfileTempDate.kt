package com.aoztg.greengrim.presentation.ui.mypage.myprofile

import java.time.LocalDate

object MyProfileTempDate {
    private var tempDate : LocalDate = LocalDate.now()

    fun setTempDate(date: LocalDate){
        tempDate = date
    }

    fun getTempDate(): LocalDate {
        val date = tempDate
        tempDate = LocalDate.now()
        return date
    }
}