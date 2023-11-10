package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.CertificationDatesResponse
import com.aoztg.greengrim.data.model.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.CertificationDetailResponse
import com.aoztg.greengrim.data.model.CertificationListResponse
import com.aoztg.greengrim.data.model.CreateCertificationRequest
import com.aoztg.greengrim.data.model.MyCertificationListResponse
import com.aoztg.greengrim.data.remote.CertificationAPI
import retrofit2.Response
import javax.inject.Inject

class CertificationRepositoryImpl @Inject constructor(private val api: CertificationAPI): CertificationRepository {

    override suspend fun createCertification(data: CreateCertificationRequest): Response<Unit> = api.createCertification(data)

    override suspend fun deleteCertification(id: Int): Response<Unit> = api.deleteCertification(id)

    override suspend fun getCertificationDefaultData(id: Int): Response<CertificationDefaultDataResponse> = api.getCertificationDefaultData(id)

    override suspend fun getCertificationDate(
        challengeId: Int,
        month: String
    ): Response<CertificationDatesResponse> = api.getCertificationDate(challengeId, month)

    override suspend fun getCertificationList(
        challengeId: Int,
        date: String,
        page: Int,
        size: Int
    ): Response<CertificationListResponse> = api.getCertificationList(challengeId,date, page, size)

    override suspend fun getCertificationDetail(id: Int): Response<CertificationDetailResponse> = api.getCertificationDetail(id)

    override suspend fun getMyCertificationDate(month: String): Response<CertificationDatesResponse> = api.getMyCertificationDate(month)

    override suspend fun getMyCertificationList(
        date: String,
        page: Int,
        size: Int
    ): Response<MyCertificationListResponse> = api.getMyCertificationList(date, page, size)

}