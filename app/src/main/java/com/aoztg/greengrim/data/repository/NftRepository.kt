package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse

interface NftRepository {

    suspend fun createWallet(
        body: CreateWalletRequest
    ): BaseState<Unit>

    suspend fun checkWalletExist(): BaseState<CheckWalletExistResponse>

    suspend fun getWalletInfo(): BaseState<GetWalletInfoResponse>
}