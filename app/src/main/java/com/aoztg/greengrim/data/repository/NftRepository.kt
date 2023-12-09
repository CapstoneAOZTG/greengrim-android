package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.request.DrawGrimRequest
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.CreateNftResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse
import com.aoztg.greengrim.data.model.response.GrimDetailResponse
import com.aoztg.greengrim.data.model.response.GrimListResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NftRepository {

    suspend fun createWallet(
        body: CreateWalletRequest
    ): BaseState<Unit>

    suspend fun checkWalletExist(): BaseState<CheckWalletExistResponse>

    suspend fun getWalletInfo(): BaseState<GetWalletInfoResponse>

    suspend fun getGrimList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<GrimListResponse>

    suspend fun getMyGrimList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<GrimListResponse>

    suspend fun drawGrim(
        body: DrawGrimRequest
    ): BaseState<Unit>


    suspend fun getGrimDetail(
        id: Int
    ): BaseState<GrimDetailResponse>


    suspend fun createNft(
        body : CreateNftRequest
    ): BaseState<CreateNftResponse>


    suspend fun getNftDetail(
        id: Int
    ): BaseState<NftDetailResponse>


    suspend fun getMyNftList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse>


    suspend fun getMoreNft(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse>


    suspend fun getHotNfts(): BaseState<NftListResponse>
}