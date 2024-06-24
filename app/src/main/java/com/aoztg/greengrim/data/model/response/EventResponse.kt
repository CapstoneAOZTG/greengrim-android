package com.aoztg.greengrim.data.model.response

data class EventResponse(
    val title: String,
    val imgUrl: String,
    val resourceId: Long,
    val url: String,
    val webView: Boolean
)
