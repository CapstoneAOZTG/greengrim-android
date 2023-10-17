package com.aoztg.greengrim.data.model

import com.google.gson.annotations.SerializedName

open class BaseResponse (
    @SerializedName("code") val code : Int = 0,
    @SerializedName("message") val message : String = ""
)