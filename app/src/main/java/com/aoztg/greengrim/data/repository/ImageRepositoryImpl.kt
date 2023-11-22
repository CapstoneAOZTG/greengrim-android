package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.ImageResponse
import com.aoztg.greengrim.data.remote.ImageAPI
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(private val api: ImageAPI) : ImageRepository {

    override suspend fun imageToUrl(
        data: MultipartBody.Part
    ): Response<ImageResponse> = api.imageToUrl(data)
}