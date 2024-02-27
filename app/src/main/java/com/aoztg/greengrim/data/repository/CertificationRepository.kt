package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.CertificationDatesResponse
import com.aoztg.greengrim.data.model.response.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CreateCertificationResponse
import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import retrofit2.Response

interface CertificationRepository {

    suspend fun getCertificationDefaultData(
        id: Long
    ): BaseState<CertificationDefaultDataResponse>

    suspend fun getCertificationDetail(
        id: Long
    ): BaseState<CertificationDetailResponse>

    suspend fun getCertificationDate(
        challengeId: Long,
    ): BaseState<CertificationDatesResponse>

    suspend fun getCertificationList(
        challengeId: Long,
        date: String,
        page: Int,
        size: Int
    ): BaseState<CertificationListResponse>

    suspend fun getMyCertificationDate(): BaseState<CertificationDatesResponse>

    suspend fun getMyCertificationList(
        date: String,
        page: Int,
        size: Int
    ): BaseState<MyCertificationListResponse>

    suspend fun verifyCertification(
        data: VerificationsRequest
    ): BaseState<Unit>

    suspend fun createCertification(
        data: CreateCertificationRequest
    ): BaseState<CreateCertificationResponse>

    suspend fun deleteCertification(
        id: Long
    ): BaseState<Unit>
}