package com.aoztg.greengrim.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface FcmAPI {

    @GET("/visitor/fcm/subscribe")
    suspend fun subscribeFcm(): Response<Unit>

    @GET("/visitor/fcm/unsubscribe")
    suspend fun unsubscribeFcm(): Response<Unit>
}