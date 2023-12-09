package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.request.DrawGrimRequest
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.CreateNftResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse
import com.aoztg.greengrim.data.model.response.GrimDetailResponse
import com.aoztg.greengrim.data.model.response.GrimListResponse
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.MyGrimForNftResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NftAPI {

    @POST("/visitor/wallets")
    suspend fun createWallet(
        @Body params: CreateWalletRequest
    ): Response<Unit>

    @GET("/visitor/wallets")
    suspend fun checkWalletExist(): Response<CheckWalletExistResponse>

    @GET("/member/wallets")
    suspend fun getWalletInfo(): Response<GetWalletInfoResponse>

    @GET("/grims")
    suspend fun getGrimList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<GrimListResponse>

    @GET("/visitor/grims")
    suspend fun getMyGrimList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<GrimListResponse>

    @POST("/visitor/grims")
    suspend fun drawGrim(
        @Body params: DrawGrimRequest
    ): Response<Unit>

    @GET("/grims/{id}")
    suspend fun getGrimDetail(
        @Path("id") id: Int
    ): Response<GrimDetailResponse>

    @POST("/memeber/nfts")
    suspend fun createNft(
        @Body params: CreateNftRequest
    ): Response<CreateNftResponse>

    @GET("/nfts/{id}")
    suspend fun getNftDetail(
        @Path("id") id: Int
    ): Response<NftDetailResponse>

    @GET("/member/nfts")
    suspend fun getMyNftList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<NftListResponse>

    @GET("/hot-nfts")
    suspend fun getMoreNft(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<NftListResponse>

    @GET("/home/nfts")
    suspend fun getHotNfts(): Response<HotNftResponse>

    @GET("/visitor/grims/nfts")
    suspend fun getMyGrimForNft(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<MyGrimForNftResponse>

}