package com.aoztg.greengrim.data.config

interface KeyDataStoreManager {

    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getMemberId(): Long?
    suspend fun getSocialType(): String?

    suspend fun putAccessToken(token: String)
    suspend fun putRefreshToken(token: String)
    suspend fun putMemberId(id: Long)
    suspend fun putSocialType(type: String)

    suspend fun deleteAccessToken()
    suspend fun deleteRefreshToken()
    suspend fun deleteMemberId()
    suspend fun deleteSocialType()

}