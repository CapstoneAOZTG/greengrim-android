package com.aoztg.greengrim.data.model

import com.google.gson.Gson
import retrofit2.Response

sealed class BaseState<out T>{
    data class Success<out T>(val body: T): BaseState<T>()
    data class Error(val msg: String, val code: String): BaseState<Nothing>()
}

internal fun <T> runRemote(response: Response<T>): BaseState<T>{
    return try{
        if(response.isSuccessful) {
            response.body()?.let{
                BaseState.Success(it)
            } ?: run{
                BaseState.Error("EMPTY","응답이 비어있습니다")
            }
        } else {
            val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            BaseState.Error(error.message, error.code)
        }
    } catch(e: Exception){
        BaseState.Error("EMPTY","네트워크 통신 에러")
    }
}

