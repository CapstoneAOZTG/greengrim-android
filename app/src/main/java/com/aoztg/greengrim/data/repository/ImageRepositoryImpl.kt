package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.ImageResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.ImageAPI
import okhttp3.MultipartBody
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(private val api: ImageAPI) : ImageRepository {

    override suspend fun imageToUrl(
        data: MultipartBody.Part
    ): BaseState<ImageResponse> = runRemote { api.imageToUrl(data) }
}