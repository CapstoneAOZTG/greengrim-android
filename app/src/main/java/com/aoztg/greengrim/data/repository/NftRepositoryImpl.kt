package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.response.CreateNftResponse
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.NftAPI
import javax.inject.Inject

class NftRepositoryImpl @Inject constructor(
    private val api: NftAPI
) : NftRepository {

    override suspend fun getHotNfts(): BaseState<HotNftResponse> = runRemote { api.getHotNfts() }

    override suspend fun createNft(body: CreateNftRequest): BaseState<CreateNftResponse> =
        runRemote { api.createNft(body) }

    override suspend fun getMoreNft(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse> = runRemote { api.getMoreNft(page, size, sort) }

    override suspend fun getNftDetail(id: Long): BaseState<NftDetailResponse> {
        TODO("Not yet implemented")
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