package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.request.DrawGrimRequest
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse
import com.aoztg.greengrim.data.model.response.GrimListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NftAPI {

    @POST("/visitor/wallets")
    suspend fun createWallet(
        @Body params: CreateWalletRequest
    ): Response<Unit>

    @GET("/visitor/wallets")
    suspend fun checkWalletExist(): Response<CheckWalletExistResponse>

    @GET("/member/wallets")
    suspend fun getWalletInfo(): Response<GetWalletInfoResponse>

    @GET("/grims")
    suspend fun getGrimList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<GrimListResponse>

    @GET("/visitor/grims")
    suspend fun getMyGrimList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<GrimListResponse>

    @POST("/visitor/grims")
    suspend fun drawGrim(
        @Body params: DrawGrimRequest
    ): Response<Unit>
}