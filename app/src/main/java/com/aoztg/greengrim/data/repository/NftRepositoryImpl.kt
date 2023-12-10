package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CheckPasswordRequest
import com.aoztg.greengrim.data.model.request.CreateNftRequest
import com.aoztg.greengrim.data.model.request.CreateWalletRequest
import com.aoztg.greengrim.data.model.request.DrawGrimRequest
import com.aoztg.greengrim.data.model.request.PatchGrimNameRequest
import com.aoztg.greengrim.data.model.request.PurchaseNftRequest
import com.aoztg.greengrim.data.model.request.SellNftRequest
import com.aoztg.greengrim.data.model.response.CheckPasswordResponse
import com.aoztg.greengrim.data.model.response.CheckWalletExistResponse
import com.aoztg.greengrim.data.model.response.CreateNftResponse
import com.aoztg.greengrim.data.model.response.GetWalletInfoResponse
import com.aoztg.greengrim.data.model.response.GrimDetailResponse
import com.aoztg.greengrim.data.model.response.GrimListResponse
import com.aoztg.greengrim.data.model.response.HotNftResponse
import com.aoztg.greengrim.data.model.response.InfoBeforePurchaseNftResponse
import com.aoztg.greengrim.data.model.response.InfoBeforeSellNftResponse
import com.aoztg.greengrim.data.model.response.MyGrimForNftResponse
import com.aoztg.greengrim.data.model.response.NftDetailResponse
import com.aoztg.greengrim.data.model.response.NftListResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.NftAPI
import javax.inject.Inject

class NftRepositoryImpl @Inject constructor(
    private val api: NftAPI
) : NftRepository {

    override suspend fun createWallet(body: CreateWalletRequest): BaseState<Unit> =
        runRemote { api.createWallet(body) }

    override suspend fun checkWalletExist(): BaseState<CheckWalletExistResponse> =
        runRemote { api.checkWalletExist() }

    override suspend fun getWalletInfo(): BaseState<GetWalletInfoResponse> =
        runRemote { api.getWalletInfo() }

    override suspend fun getGrimList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<GrimListResponse> = runRemote { api.getGrimList(page, size, sort) }

    override suspend fun getMyGrimList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<GrimListResponse> = runRemote { api.getMyGrimList(page, size, sort) }

    override suspend fun drawGrim(body: DrawGrimRequest): BaseState<Unit> =
        runRemote { api.drawGrim(body) }

    override suspend fun getHotNfts(): BaseState<HotNftResponse> = runRemote { api.getHotNfts() }

    override suspend fun createNft(body: CreateNftRequest): BaseState<CreateNftResponse> =
        runRemote { api.createNft(body) }

    override suspend fun getGrimDetail(id: Int): BaseState<GrimDetailResponse> =
        runRemote { api.getGrimDetail(id) }

    override suspend fun getMoreNft(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse> = runRemote { api.getMoreNft(page, size, sort) }

    override suspend fun getMyNftList(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<NftListResponse> = runRemote { api.getMyNftList(page, size, sort) }

    override suspend fun getNftDetail(id: Int): BaseState<NftDetailResponse> =
        runRemote { api.getNftDetail(id) }

    override suspend fun getMyGrimForNft(page: Int, size: Int): BaseState<MyGrimForNftResponse> =
        runRemote { api.getMyGrimForNft(page,size) }

    override suspend fun patchGrimTitle(body: PatchGrimNameRequest): BaseState<Unit> =
        runRemote { api.patchGrimTitle(body) }

    override suspend fun checkPassword(body: CheckPasswordRequest): BaseState<CheckPasswordResponse> =
        runRemote { api.checkPassword(body) }

    override suspend fun purchaseNft(body: PurchaseNftRequest): BaseState<Unit> =
        runRemote { api.purchaseNft(body) }

    override suspend fun sellNft(body: SellNftRequest): BaseState<Unit> =
        runRemote { api.sellNft(body) }

    override suspend fun getInfoBeforeSellNft(id: Int): BaseState<InfoBeforeSellNftResponse> =
        runRemote { api.getInfoBeforeSellNft(id) }

    override suspend fun getInfoBeforePurchaseNft(id: Int): BaseState<InfoBeforePurchaseNftResponse> =
        runRemote { api.getInfoBeforePurchaseNft(id) }
}