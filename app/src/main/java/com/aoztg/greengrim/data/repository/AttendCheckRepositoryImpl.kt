package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.remote.AttendCheckAPI
import retrofit2.Response
import javax.inject.Inject

class AttendCheckRepositoryImpl @Inject constructor(
    private val api: AttendCheckAPI
): AttendCheckRepository {

    override suspend fun verifyCertification(data: VerificationsRequest): Response<Unit> = api.verifyCertification(data)
    override suspend fun getCertificationForVerify(): Response<CertificationDetailResponse> = api.getCertificationForVerify()

}