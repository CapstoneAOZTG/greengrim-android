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

    suspend fun getHotNfts(): BaseState<HotNftResponse>

    suspend fun getMyGrimForNft(
        page: Int,
        size: Int
    ): BaseState<MyGrimForNftResponse>

    suspend fun patchGrimTitle(
        body: PatchGrimNameRequest
    ): BaseState<Unit>

    suspend fun checkPassword(
        body: CheckPasswordRequest
    ): BaseState<CheckPasswordResponse>

    suspend fun purchaseNft(
        body: PurchaseNftRequest
    ): BaseState<Unit>

    suspend fun sellNft(
        body: SellNftRequest
    ): BaseState<Unit>

    suspend fun getInfoBeforeSellNft(
        id: Int
    ): BaseState<InfoBeforeSellNftResponse>

    suspend fun getInfoBeforePurchaseNft(
        id: Int
    ): BaseState<InfoBeforePurchaseNftResponse>
}