package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.response.StockNftResponse

interface NftRepository {

    suspend fun getStockNftList(
        grade: String
    ): BaseState<StockNftResponse>

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

}