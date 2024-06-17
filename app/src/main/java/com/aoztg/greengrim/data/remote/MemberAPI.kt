package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.AccusationRequest
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.request.WalletInfoRequest
import com.aoztg.greengrim.data.model.response.AlarmListResponse
import com.aoztg.greengrim.data.model.response.AuthData
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.response.EventResponse
import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.response.HomeMyInfoResponse
import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.data.model.response.MyPointResponse
import com.aoztg.greengrim.data.model.response.RecentIssueDetailResponse
import com.aoztg.greengrim.data.model.response.RecentIssueLIstResponse
import com.aoztg.greengrim.data.model.response.RecentIssueResponse
import com.aoztg.greengrim.data.model.response.TokenData
import com.aoztg.greengrim.data.model.response.WalletInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberAPI {

    @PATCH("/visitor/members/refresh")
    suspend fun refreshToken(
        @Header("refreshToken") refreshToken: String
    ): Response<TokenData>

    @GET("/visitor/fcm/subscribe")
    suspend fun subscribeFcm(): Response<Unit>

    @GET("/visitor/fcm/unsubscribe")
    suspend fun unsubscribeFcm(): Response<Unit>

    @POST("/sign-up")
    suspend fun signup(
        @Body params: SignupRequest
    ): Response<AuthData>

    @POST("/login")
    suspend fun login(
        @Body params: LoginRequest
    ): Response<AuthData>

    @POST("/nick-name")
    suspend fun checkNick(
        @Body params: CheckNickRequest
    ): Response<CheckNickResponse>

    @GET("/visitor/members/profile")
    suspend fun getProfile(): Response<GetProfileResponse>

    @PATCH("/visitor/members/profile")
    suspend fun patchProfile(
        @Body params: PatchProfileRequest
    ): Response<Unit>

    @GET("/visitor/members/my")
    suspend fun getMyInfo(): Response<MyInfoResponse>

    @GET("/visitor/members/profile")
    suspend fun getMemberInfo(
        @Query("memberId") memberId: Long
    ): Response<GetProfileResponse>

    @GET("/visitor/wallets")
    suspend fun getMyWalletInfo(): Response<WalletInfoResponse>

    @POST("/visitor/wallets")
    suspend fun addWallet(
        @Body params: WalletInfoRequest
    ): Response<Unit>

    @POST("/member/wallets")
    suspend fun editWallet(
        @Body params: WalletInfoRequest
    ): Response<Unit>

    @POST("/visitor/logout")
    suspend fun logout(): Response<Unit>

    @DELETE("/visitor/members/delete")
    suspend fun withdraw(): Response<Unit>

    @POST("/visitor/points")
    suspend fun getMyPoint(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<MyPointResponse>

    @GET("/visitor/events/home")
    suspend fun getEvent(): Response<EventResponse>

    @GET("/visitor/issues/home")
    suspend fun getHomeIssues(): Response<RecentIssueResponse>

    @GET("/visitor/issues")
    suspend fun getIssueList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<RecentIssueLIstResponse>

    @GET("/visitor/issues/{id}")
    suspend fun getIssueDetail(
        @Path("id") id : Long,
    ): Response<RecentIssueDetailResponse>

    @GET("/visitor/members/home")
    suspend fun getHomeMyInfo(): Response<HomeMyInfoResponse>

    @POST("/visitor/reports")
    suspend fun accusation(
        @Query("type") type: String,
        @Body body: AccusationRequest
    ): Response<Unit>

    @POST("/visitor/hiding/member")
    suspend fun hideMember(
        @Query("id") id: Long
    ): Response<Unit>

    @GET("/visitor/alarms")
    suspend fun getAlarmList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<AlarmListResponse>

}