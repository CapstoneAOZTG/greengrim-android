package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.EditNftRequest
import com.aoztg.greengrim.data.model.request.NftLikeRequest
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.NftCollectionCountResponse
import com.aoztg.greengrim.data.model.response.NftCollectionResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.response.NftSimpleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NftAPI {

    @GET("/visitor/nfts/stock")
    suspend fun getStockNftList(
        @Query("grade") grade : String
    ): Response<NftSimpleResponse>

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
    suspend fun getNftCollectionCount(): Response<NftCollectionCountResponse>

    @GET("/visitor/nfts/collection")
    suspend fun getNftCollection(
        @Query("grade") grade: String,
        @Query("page") page : Int,
        @Query("size") size : Int,
    ) : Response<NftCollectionResponse>

    @GET("/visitor/nfts/stock")
    suspend fun getNftForExchange(
        @Query("grade") grade : String
    ) : Response<NftSimpleResponse>

    @GET("/visitor/nfts/stock/refresh")
    suspend fun getNftForExchangeRefresh(
        @Query("grade") grade : String,
        @Query("nftList") nftList : List<Long>
    ): Response<NftSimpleResponse>

    @POST("/visitor/nfts/{id}")
    suspend fun exchangeNft(
        @Path("id") id : Long
    ): Response<Unit>

    @PATCH("/visitor/nfts")
    suspend fun editExchangedNft(
        @Body params : EditNftRequest
    ): Response<NftDetailResponse>

    @POST("/visitor/nft-likes")
    suspend fun nftLike(
        @Body params : NftLikeRequest
    ) : Response<Unit>

    @GET("/home/hot-nfts")
    suspend fun getHotNft():Response<HotNftResponse>

    @POST("/visitor/hiding/nft")
    suspend fun hideNft(
        @Query("id") id : Long
    ): Response<Unit>

    @DELETE("/visitor/nfts/{id}")
    suspend fun deleteNft(
        @Query("id") id : Long
    ): Response<Unit>

}