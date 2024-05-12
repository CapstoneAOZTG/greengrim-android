package com.aoztg.greengrim.data.model.request

data class AccusationRequest(
    val resourceId: Long,
    val reason: String,
    val content : String
)
