package com.aoztg.greengrim.presentation.ui.global.mapper

import com.aoztg.greengrim.data.model.response.AlarmItem
import com.aoztg.greengrim.presentation.ui.global.model.UiAlarmData

fun AlarmItem.toUiAlarmData() = UiAlarmData(
    type = alarmType,
    resourceId = resourceId,
    imgUrl = imgUrl,
    category = alarmCategory,
    createdAt = createdAt,
    boldDescription = variableContent,
    description = fixedContent,
    isChecked = checked,
    memberId = memberId
)
