package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState

interface FcmRepository {

    suspend fun subscribeFcm(): BaseState<Unit>

    suspend fun unsubscribeFcm(): BaseState<Unit>
}