package com.aoztg.greengrim.data.model.response

data class AlarmListResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<AlarmItem>
)

data class AlarmItem(
    val alarmType: String,
    val alarmCategory: String,
    val imgUrl: String,
    val createdAt: String,
    val resourceId: Long,
    val variableContent: String,
    val fixedContent: String,
    val memberId: Long,
    val checked: Boolean
)
