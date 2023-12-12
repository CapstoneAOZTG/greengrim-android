package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.ImageResponse
import okhttp3.MultipartBody

interface ImageRepository {

    suspend fun imageToUrl(
        data: MultipartBody.Part,
    ): BaseState<ImageResponse>
}