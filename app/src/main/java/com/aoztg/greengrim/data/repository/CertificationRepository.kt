package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.CertificationDatesResponse
import com.aoztg.greengrim.data.model.response.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import retrofit2.Response

interface CertificationRepository {

    suspend fun getCertificationDefaultData(
        id: Int
    ): Response<CertificationDefaultDataResponse>

    suspend fun getCertificationDetail(
        id: Int
    ): Response<CertificationDetailResponse>

    suspend fun getCertificationDate(
        challengeId: Int,
    ): Response<CertificationDatesResponse>

    suspend fun getCertificationList(
        challengeId: Int,
        date: String,
        page: Int,
        size: Int
    ): Response<CertificationListResponse>

    suspend fun getMyCertificationDate(): Response<CertificationDatesResponse>

    suspend fun getMyCertificationList(
        date: String,
        page: Int,
        size: Int
    ): Response<MyCertificationListResponse>

    suspend fun verifyCertification(
        data: VerificationsRequest
    ): Response<Unit>

    suspend fun createCertification(
        data: CreateCertificationRequest
    ): Response<Unit>

    suspend fun deleteCertification(
        id: Int
    ): Response<Unit>
}