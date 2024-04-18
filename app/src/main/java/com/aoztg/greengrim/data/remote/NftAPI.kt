package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.NftCategoryResponse
import com.aoztg.greengrim.data.model.response.NftCollectionCountResponse
import com.aoztg.greengrim.data.model.response.NftCollectionResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.response.StockNftResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NftAPI {

    @GET("/visitor/nfts/stock")
    suspend fun getStockNftList(
        @Query("grade") grade : String
    ): Response<StockNftResponse>

    @GET("/visitor/nfts")
    suspend fun getExchangedNftList(
        @Query("page") page : Int,
        @Query("size") size : Int,
        @Query("sort") sort : String
    ): Response<NftListResponse>

    @GET("/nfts/{id}")
    suspend fun getNftDetail(
        @Path("id") id : Long
    ): Response<NftDetailResponse>

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

    @GET("/visitor/nfts/stock/amount")
    suspend fun getNftCategory(): Response<NftCategoryResponse>

    @GET("/visitor/nfts/collection")
    suspend fun getNftCollection(
        @Query("grade") grade: String,
        @Query("page") page : Int,
        @Query("size") size : Int,
    ) : Response<NftCollectionResponse>

    @GET("/visitor/nfts/stock/amount")
    suspend fun getNftCollectionCount(): Response<NftCollectionCountResponse>

}