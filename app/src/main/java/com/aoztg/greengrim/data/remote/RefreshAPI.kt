package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.PATCH

interface RefreshAPI {

    @PATCH("/visitor/refresh")
    suspend fun refreshToken(
        @Header("refreshToken") refreshToken: String
    ): Response<LoginResponse>
}