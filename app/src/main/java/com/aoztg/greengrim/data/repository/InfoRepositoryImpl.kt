package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.request.WalletInfoRequest
import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.data.model.response.MyKeywordsResponse
import com.aoztg.greengrim.data.model.response.WalletInfoResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.InfoAPI
import javax.inject.Inject

class InfoRepositoryImpl @Inject constructor(private val api: InfoAPI) : InfoRepository {

    override suspend fun getProfile(): BaseState<GetProfileResponse> =
        runRemote { api.getProfile() }

    override suspend fun patchProfile(data: PatchProfileRequest): BaseState<Unit> =
        runRemote { api.patchProfile(data) }

    override suspend fun withdrawal(): BaseState<Unit> = runRemote { api.withdrawal() }

    override suspend fun getMyInfo(): BaseState<MyInfoResponse> = runRemote { api.getMyInfo() }

    override suspend fun getMyKeywords(): BaseState<MyKeywordsResponse> = runRemote { api.getMyKeywords() }

    override suspend fun getMyWalletInfo(): BaseState<WalletInfoResponse> = runRemote { api.getMyWalletInfo() }

    override suspend fun addWallet(body: WalletInfoRequest): BaseState<Unit> = runRemote { api.addWallet(body) }

    override suspend fun editWallet(body: WalletInfoRequest): BaseState<Unit> = runRemote { api.editWallet(body) }

}