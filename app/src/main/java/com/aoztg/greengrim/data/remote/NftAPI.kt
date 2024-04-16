package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.response.CreateNftResponse
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NftAPI {

    @POST("/member/nfts")
    suspend fun createNft(
        @Body params: CreateNftRequest
    ): Response<CreateNftResponse>

    @GET("/nfts/{id}")
    suspend fun getNftDetail(
        @Path("id") id: Long
    ): Response<NftDetailResponse>

    @GET("/hot-nfts")
    suspend fun getMoreNft(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<NftListResponse>

    @GET("/home/nfts")
    suspend fun getHotNfts(): Response<HotNftResponse>

    @GET("/visitor/nfts/profile")
    suspend fun getMyNftList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<NftListResponse>

    @GET("/visitor/nfts/profile")
    suspend fun getMemberNftList(
        @Query("memberId") memberId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<NftListResponse>

}