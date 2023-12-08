package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.NftAPI
import javax.inject.Inject

class NftRepositoryImpl @Inject constructor(
    private val api: NftAPI
): NftRepository {

    override suspend fun createWallet(body: CreateWalletRequest): BaseState<Unit> =
        runRemote { api.createWallet(body) }

    override suspend fun checkWalletExist(): BaseState<CheckWalletExistResponse> =
        runRemote { api.checkWalletExist() }

    override suspend fun getWalletInfo(): BaseState<GetWalletInfoResponse> =
        runRemote { api.getWalletInfo() }
}