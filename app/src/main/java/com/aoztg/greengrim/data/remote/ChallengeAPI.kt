package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.request.SearchChallengeRequest
import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.data.model.response.CreateChallengeResponse
import com.aoztg.greengrim.data.model.response.HotChallengeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChallengeAPI {

    @GET("/visitor/challenges/home")
    suspend fun getHotChallenges(): Response<HotChallengeResponse>

    @GET("/visitor/challenges/hot-challenges")
    suspend fun getMoreHotChallenges(
        @Query("option") option: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ChallengeListResponse>

    @POST("/visitor/challenges")
    suspend fun createChallenge(
        @Body params: CreateChallengeRequest
    ): Response<CreateChallengeResponse>

    @GET("/visitor/challenges/{id}")
    suspend fun getChallengeDetail(
        @Path("id") id: Long
    ): Response<ChallengeDetailResponse>

    @GET("/visitor/challenges")
    suspend fun getChallengeList(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<ChallengeListResponse>

    @POST("/visitor/challenges/exit")
    suspend fun exitChallenge(
        @Query("id") id: Long
    ): Response<Unit>

    @POST("/visitor/point")
    suspend fun postPoint(): Response<Unit>

    @POST("/visitor/challenges/searches")
    suspend fun searchWholeChallenge(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body params: SearchChallengeRequest
    ): Response<ChallengeListResponse>

    @POST("/visitor/challenges/searches")
    suspend fun searchChallenge(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body params: SearchChallengeRequest
    ): Response<ChallengeListResponse>

    @GET("/visitor/challenges/members")
    suspend fun getMyChallengeList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<ChallengeListResponse>

    @GET("/visitor/challenges/members")
    suspend fun getMemberChallengeList(
        @Query("memberId") memberId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<ChallengeListResponse>

    @POST("/visitor/hiding/challenge")
    suspend fun hideChallenge(
        @Query("id") id: Long
    ): Response<Unit>
}