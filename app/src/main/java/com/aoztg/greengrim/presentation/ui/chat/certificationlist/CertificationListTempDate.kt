package com.aoztg.greengrim.presentation.ui.chat.certificationlist

import java.time.LocalDate

object CertificationListTempDate {
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