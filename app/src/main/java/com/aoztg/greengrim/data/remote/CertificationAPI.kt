package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.CertificationDatesResponse
import com.aoztg.greengrim.data.model.CertificationDefaultDataResponse
import com.aoztg.greengrim.data.model.CertificationDetailResponse
import com.aoztg.greengrim.data.model.CertificationListResponse
import com.aoztg.greengrim.data.model.CreateCertificationRequest
import com.aoztg.greengrim.data.model.MyCertificationListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CertificationAPI {

    @GET("/certifications/preview/{id}")
    suspend fun getCertificationDefaultData(
        @Path("id") id: Int
    ): Response<CertificationDefaultDataResponse>

    @GET("/certifications/{id}")
    suspend fun getCertificationDetail(
        @Path("id") id: Int
    ): Response<CertificationDetailResponse>

    @GET("/certifications/month")
    suspend fun getCertificationDate(
        @Query("challengeId") challengeId: Int,
        @Query("month") month: String
    ): Response<CertificationDatesResponse>

    @GET("/certifications/date")
    suspend fun getCertificationList(
        @Query("challengeId") challengeId: Int,
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<CertificationListResponse>

    @GET("/visitor/certifications/month")
    suspend fun getMyCertificationDate(
        @Query("month") month: String
    ): Response<CertificationDatesResponse>

    @GET("/visitor/certifications/date")
    suspend fun getMyCertificationList(
        @Query("date") date: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<MyCertificationListResponse>

    @POST("/visitor/certifications")
    suspend fun createCertification(
        @Body params: CreateCertificationRequest
    ): Response<Unit>

    @POST("/visitor/certifications/{id}")
    suspend fun deleteCertification(
        @Path("id") id: Int
    ): Response<Unit>

}