package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.AttendCheckAPI
import javax.inject.Inject

class AttendCheckRepositoryImpl @Inject constructor(
    private val api: AttendCheckAPI
) : AttendCheckRepository {

    override suspend fun verifyCertification(data: VerificationsRequest): BaseState<Unit> {
        return runRemote { api.verifyCertification(data) }
    }

    override suspend fun getCertificationForVerify(): BaseState<CertificationDetailResponse> {
        return runRemote { api.getCertificationForVerify() }
    }

}