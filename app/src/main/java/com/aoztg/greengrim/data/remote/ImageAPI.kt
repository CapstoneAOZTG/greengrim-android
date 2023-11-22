package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.ImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageAPI {

    @Multipart
    @POST("/file")
    suspend fun imageToUrl(
        @Part images: MultipartBody.Part,
    ): Response<ImageResponse>
}