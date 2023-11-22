package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.response.CreateChallengeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChallengeAPI {

    @POST("/visitor/challenges")
    suspend fun createChallenge(
        @Body params: CreateChallengeRequest
    ): Response<CreateChallengeResponse>

    @GET("/challenges/{id}")
    suspend fun getChallengeDetail(
        @Path("id") id : Int
    ): Response<ChallengeDetailResponse>

    @GET("/challenges")
    suspend fun getChallengeList(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort : String
    ): Response<ChallengeListResponse>

    @GET("/visitor/challenges")
    suspend fun getMyChallengeList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort : String
    ): Response<ChallengeListResponse>
}