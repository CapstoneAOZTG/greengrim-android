package com.aoztg.greengrim.presentation.ui.info.mycertification

import java.time.LocalDate

object MyCertificationTempDate {
    private var tempDate : LocalDate = LocalDate.now()

    fun setTempDate(date: LocalDate){
        tempDate = date
    }

    fun getTempDate(): LocalDate{
        val date = tempDate
        tempDate = LocalDate.now()
        return date
    }
}