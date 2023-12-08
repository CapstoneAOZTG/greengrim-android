package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NftAPI {

    @POST("/visitor/wallets")
    fun createWallet(
        @Body params : CreateWalletRequest
    ): Response<Unit>
}