package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.response.StockNftResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.NftAPI
import javax.inject.Inject

class NftRepositoryImpl @Inject constructor(
    private val api: NftAPI
) : NftRepository {

    override suspend fun getStockNftList(grade: String): BaseState<StockNftResponse> =
        runRemote { api.getStockNftList(grade) }

    override suspend fun getExchangedNftList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse> =
        runRemote { api.getExchangedNftList(page, size, sort) }

    override suspend fun getNftDetail(id: Long): BaseState<NftDetailResponse> = runRemote {
        api.getNftDetail(id)
    }

    override suspend fun getMemberNftList(
        memberId: Long,
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse> =
        runRemote { api.getMemberNftList(memberId, page, size, sort) }

    override suspend fun getMyNftList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse> =
        runRemote { api.getMyNftList(page, size, sort) }

}