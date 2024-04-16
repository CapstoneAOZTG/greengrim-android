package com.aoztg.greengrim.data.model.response

data class MyPointResponse(
    val page: Int,
    val hasNext: Boolean,
    val result : List<PointInfo>
)

data class PointInfo(
    val id : Long,
    val title: String,
    val imgUrl: String,
    val historyOption: String,
    val point: Int,
    val totalPoint : Int,
    val date : String
)
