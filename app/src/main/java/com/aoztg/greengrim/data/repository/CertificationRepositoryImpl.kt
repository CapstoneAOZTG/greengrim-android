package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.model.response.CertificationDatesResponse
import com.aoztg.greengrim.data.model.response.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import com.aoztg.greengrim.data.remote.CertificationAPI
import retrofit2.Response
import javax.inject.Inject

class CertificationRepositoryImpl @Inject constructor(private val api: CertificationAPI): CertificationRepository {

    override suspend fun createCertification(data: CreateCertificationRequest): Response<Unit> = api.createCertification(data)

    override suspend fun deleteCertification(id: Int): Response<Unit> = api.deleteCertification(id)

    override suspend fun getCertificationDefaultData(id: Int): Response<CertificationDefaultDataResponse> = api.getCertificationDefaultData(id)

    override suspend fun getCertificationDate(
        challengeId: Int
    ): Response<CertificationDatesResponse> = api.getCertificationDate(challengeId)

    override suspend fun getCertificationList(
        challengeId: Int,
        date: String,
        page: Int,
        size: Int
    ): Response<CertificationListResponse> = api.getCertificationList(challengeId,date, page, size)

    override suspend fun getCertificationDetail(id: Int): Response<CertificationDetailResponse> = api.getCertificationDetail(id)

    override suspend fun getMyCertificationDate(): Response<CertificationDatesResponse> = api.getMyCertificationDate()

    override suspend fun getMyCertificationList(
        date: String,
        page: Int,
        size: Int
    ): Response<MyCertificationListResponse> = api.getMyCertificationList(date, page, size)

}