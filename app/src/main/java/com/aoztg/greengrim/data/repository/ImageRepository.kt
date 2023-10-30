package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.ImageResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface ImageRepository {

    suspend fun imageToUrl(
        data: MultipartBody.Part,
    ): Response<ImageResponse>
}