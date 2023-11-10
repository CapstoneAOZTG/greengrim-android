package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.CertificationDatesResponse
import com.aoztg.greengrim.data.model.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.CertificationDetailResponse
import com.aoztg.greengrim.data.model.CertificationListResponse
import com.aoztg.greengrim.data.model.CreateCertificationRequest
import retrofit2.Response

interface ChatRepository {

    suspend fun getCertificationDefaultData(
        id: Int
    ): Response<CertificationDefaultDataResponse>

    suspend fun getCertificationDetail(
        id: Int
    ): Response<CertificationDetailResponse>

    suspend fun getCertificationDate(
        challengeId: Int,
        month: String
    ): Response<CertificationDatesResponse>

    suspend fun getCertificationList(
        challengeId: Int,
        date: String,
        page: Int,
        size: Int
    ): Response<CertificationListResponse>

    suspend fun createCertification(
        data: CreateCertificationRequest
    ): Response<Unit>

    suspend fun deleteCertification(
        id: Int
    ): Response<Unit>
}