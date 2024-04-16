package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.request.WalletInfoRequest
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.data.model.response.SignupResponse
import com.aoztg.greengrim.data.model.response.WalletInfoResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.MemberAPI
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(private val api: MemberAPI) : MemberRepository {

    override suspend fun subscribeFcm(): BaseState<Unit> = runRemote { api.subscribeFcm() }

    override suspend fun unsubscribeFcm(): BaseState<Unit> = runRemote { api.unsubscribeFcm() }

    override suspend fun getProfile(): BaseState<GetProfileResponse> =
        runRemote { api.getProfile() }

    override suspend fun patchProfile(data: PatchProfileRequest): BaseState<Unit> =
        runRemote { api.patchProfile(data) }

    override suspend fun getMyInfo(): BaseState<MyInfoResponse> = runRemote { api.getMyInfo() }

    override suspend fun getMyWalletInfo(): BaseState<WalletInfoResponse> =
        runRemote { api.getMyWalletInfo() }

    override suspend fun addWallet(body: WalletInfoRequest): BaseState<Unit> =
        runRemote { api.addWallet(body) }

    override suspend fun editWallet(body: WalletInfoRequest): BaseState<Unit> =
        runRemote { api.editWallet(body) }

    override suspend fun signup(data: SignupRequest): BaseState<SignupResponse> =
        runRemote { api.signup(data) }

    override suspend fun login(data: LoginRequest): BaseState<LoginResponse> =
        runRemote { api.login(data) }

    override suspend fun checkNick(data: CheckNickRequest): BaseState<CheckNickResponse> =
        runRemote { api.checkNick(data) }

    override suspend fun logout(): BaseState<Unit> = runRemote { api.logout() }

    override suspend fun withdraw(): BaseState<Unit> = runRemote { api.withdraw() }

}