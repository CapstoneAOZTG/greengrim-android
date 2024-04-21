package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.EditNftRequest
import com.aoztg.greengrim.data.model.request.NftLikeRequest
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.NftCollectionCountResponse
import com.aoztg.greengrim.data.model.response.NftCollectionResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.response.NftSimpleResponse

interface NftRepository {

    suspend fun getStockNftList(
        grade: String
    ): BaseState<NftSimpleResponse>

    suspend fun getExchangedNftList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse>

    suspend fun getNftDetail(
        id: Long
    ): BaseState<NftDetailResponse>

    suspend fun getMyNftList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse>

    suspend fun getMemberNftList(
        memberId: Long,
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse>

    suspend fun getNftCollectionCount(): BaseState<NftCollectionCountResponse>

    suspend fun getNftCollection(
        grade: String,
        page: Int,
        size: Int
    ): BaseState<NftCollectionResponse>

    suspend fun getNftForExchange(
        grade: String
    ): BaseState<NftSimpleResponse>

    suspend fun getNftForExchangeRefresh(
        grade: String,
        nftList: List<Long>
    ): BaseState<NftSimpleResponse>

    suspend fun exchangeNft(
        id: Long
    ): BaseState<Unit>

    suspend fun editExchangedNft(
        body: EditNftRequest
    ): BaseState<NftDetailResponse>

    suspend fun nftLike(
        body: NftLikeRequest
    ): BaseState<Unit>

    suspend fun getHotNft(): BaseState<HotNftResponse>

}