package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CertificationDatesResponse
import com.aoztg.greengrim.data.model.response.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.data.model.response.CreateCertificationResponse
import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.CertificationAPI
import javax.inject.Inject

class CertificationRepositoryImpl @Inject constructor(private val api: CertificationAPI) :
    CertificationRepository {


    override suspend fun getCertificationDefaultData(id: Int): BaseState<CertificationDefaultDataResponse> =
        runRemote { api.getCertificationDefaultData(id) }

    override suspend fun getCertificationDate(
        challengeId: Int
    ): BaseState<CertificationDatesResponse> = runRemote { api.getCertificationDate(challengeId) }

    override suspend fun getCertificationList(
        challengeId: Int,
        date: String,
        page: Int,
        size: Int
    ): BaseState<CertificationListResponse> =
        runRemote { api.getCertificationList(challengeId, date, page, size) }

    override suspend fun getCertificationDetail(id: Int): BaseState<CertificationDetailResponse> =
        runRemote { api.getCertificationDetail(id) }

    override suspend fun getMyCertificationDate(): BaseState<CertificationDatesResponse> =
        runRemote { api.getMyCertificationDate() }

    override suspend fun getMyCertificationList(
        date: String,
        page: Int,
        size: Int
    ): BaseState<MyCertificationListResponse> =
        runRemote { api.getMyCertificationList(date, page, size) }

    override suspend fun verifyCertification(data: VerificationsRequest): BaseState<Unit> =
        runRemote { api.verifyCertification(data) }

    override suspend fun createCertification(data: CreateCertificationRequest): BaseState<CreateCertificationResponse> =
        runRemote { api.createCertification(data) }

    override suspend fun deleteCertification(id: Int): BaseState<Unit> =
        runRemote { api.deleteCertification(id) }

}