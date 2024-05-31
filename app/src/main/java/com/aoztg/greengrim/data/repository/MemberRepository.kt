package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.AccusationRequest
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.request.WalletInfoRequest
import com.aoztg.greengrim.data.model.response.AlarmListResponse
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.response.EventResponse
import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.response.HomeMyInfoResponse
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.data.model.response.MyPointResponse
import com.aoztg.greengrim.data.model.response.RecentIssueResponse
import com.aoztg.greengrim.data.model.response.SignupResponse
import com.aoztg.greengrim.data.model.response.WalletInfoResponse

interface MemberRepository {

    suspend fun subscribeFcm(): BaseState<Unit>

    suspend fun unsubscribeFcm(): BaseState<Unit>

    suspend fun getProfile(): BaseState<GetProfileResponse>

    suspend fun patchProfile(
        data: PatchProfileRequest
    ): BaseState<Unit>

    suspend fun getMyInfo(): BaseState<MyInfoResponse>

    suspend fun getMemberInfo(id: Long): BaseState<GetProfileResponse>

    suspend fun getMyWalletInfo(): BaseState<WalletInfoResponse>

    suspend fun addWallet(
        body: WalletInfoRequest
    ): BaseState<Unit>

    suspend fun editWallet(
        body: WalletInfoRequest
    ): BaseState<Unit>

    suspend fun signup(
        data: SignupRequest
    ): BaseState<SignupResponse>

    suspend fun login(
        data: LoginRequest
    ): BaseState<LoginResponse>

    suspend fun checkNick(
        data: CheckNickRequest
    ): BaseState<CheckNickResponse>

    suspend fun logout(): BaseState<Unit>

    suspend fun withdraw(): BaseState<Unit>

    suspend fun getMyPointInfo(
        page: Int,
        size: Int
    ): BaseState<MyPointResponse>

    suspend fun getEvent(): BaseState<EventResponse>

    suspend fun getHomeMyInfo(): BaseState<HomeMyInfoResponse>

    suspend fun getRecentIssue(): BaseState<RecentIssueResponse>

    suspend fun refreshToken(
        refreshToken: String
    ): BaseState<LoginResponse>

    suspend fun accusation(
        type: String,
        body: AccusationRequest
    ): BaseState<Unit>

    suspend fun hideMember(
        id: Long
    ): BaseState<Unit>

    suspend fun getAlarmCheck(
        page: Int,
        size: Int
    ): BaseState<AlarmListResponse>

}