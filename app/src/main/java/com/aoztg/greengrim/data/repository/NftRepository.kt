package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.response.CreateNftResponse
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse

interface NftRepository {

    suspend fun createNft(
        body: CreateNftRequest
    ): BaseState<CreateNftResponse>

    suspend fun getNftDetail(
        id: Long
    ): BaseState<NftDetailResponse>

    suspend fun getMoreNft(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse>

    suspend fun getHotNfts(): BaseState<HotNftResponse>

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

}