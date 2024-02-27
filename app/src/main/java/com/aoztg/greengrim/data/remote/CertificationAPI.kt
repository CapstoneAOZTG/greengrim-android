package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.CertificationDatesResponse
import com.aoztg.greengrim.data.model.response.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.response.CertificationListResponse
import com.aoztg.greengrim.data.model.request.CreateCertificationRequest
import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CreateCertificationResponse
import com.aoztg.greengrim.data.model.response.MyCertificationListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CertificationAPI {

    @GET("/visitor/challenges/preview/{id}")
    suspend fun getCertificationDefaultData(
        @Path("id") id: Long
    ): Response<CertificationDefaultDataResponse>

    @GET("/certifications/{id}")
    suspend fun getCertificationDetail(
        @Path("id") id: Long
    ): Response<CertificationDetailResponse>

    @GET("/certifications/month")
    suspend fun getCertificationDate(
        @Query("challengeId") challengeId: Long
    ): Response<CertificationDatesResponse>

    @GET("/certifications/date")
    suspend fun getCertificationList(
        @Query("challengeId") challengeId: Long,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<CertificationListResponse>

    @GET("/visitor/certifications/month")
    suspend fun getMyCertificationDate(
    ): Response<CertificationDatesResponse>

    @GET("/visitor/certifications/date")
    suspend fun getMyCertificationList(
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<MyCertificationListResponse>

    @POST("/visitor/verifications")
    suspend fun verifyCertification(
        @Body params: VerificationsRequest
    ): Response<Unit>

    @POST("/visitor/certifications")
    suspend fun createCertification(
        @Body params: CreateCertificationRequest
    ): Response<CreateCertificationResponse>

    @POST("/visitor/certifications/{id}")
    suspend fun deleteCertification(
        @Path("id") id: Long
    ): Response<Unit>

}