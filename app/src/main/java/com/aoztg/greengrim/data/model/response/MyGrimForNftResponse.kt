package com.aoztg.greengrim.data.model.response

data class MyGrimForNftResponse(
    val page: Int,
    val hasNext: Boolean,
    val result: List<MyGrimSimpleData>
)

data class MyGrimSimpleData(
    val id: Long,
    val imgUrl: String,
    val title: String
)
