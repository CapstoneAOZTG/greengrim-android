package com.aoztg.greengrim.data.model

import com.google.gson.Gson
import retrofit2.Response


sealed class BaseState<out T>{
    data class Success<out T>(val body: T): BaseState<T>()
    data class Error(val msg: String): BaseState<Nothing>()
}

internal fun <T> runRemote(response: Response<T>): BaseState<T>{
    try{
        if(response.isSuccessful) {
            response.body()?.let{
                return BaseState.Success(it)
            } ?: run{
                return BaseState.Error("응답이 비어있습니다")
            }
        } else {
            val error = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
            return BaseState.Error(error.message)
        }
    } catch(e: Exception){
        return BaseState.Error("네트워크 통신 에러")
    }
}

