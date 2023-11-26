package com.aoztg.greengrim.presentation.ui.chat.chatroom

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


internal fun getCurDate(): String{
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 EEEE", Locale.KOREAN)
    return dateFormat.format(currentDate)
}

internal fun getCurTime(): String{
    val currentTime = Calendar.getInstance().time
    val timeFormat = SimpleDateFormat("a h시 mm분", Locale.KOREAN)
    return timeFormat.format(currentTime)
}