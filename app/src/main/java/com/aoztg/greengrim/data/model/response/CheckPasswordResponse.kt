package com.aoztg.greengrim.data.model.response

data class CheckPasswordResponse(
    val matched: Boolean,
    val wrongCount: Int
)
