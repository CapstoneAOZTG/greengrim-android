package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NftAPI {

    @POST("/visitor/wallets")
    suspend fun createWallet(
        @Body params : CreateWalletRequest
    ): Response<Unit>

    @GET("/visitor/wallets")
    suspend fun checkWalletExist(): Response<CheckWalletExistResponse>

    @GET("/member/wallets")
    suspend fun getWalletInfo(): Response<GetWalletInfoResponse>
}