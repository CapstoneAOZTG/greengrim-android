package com.aoztg.greengrim.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


internal fun Uri.toMultiPart(context: Context): MultipartBody.Part{
    val file = File(getRealPathFromUri(this, context)?:"")
    val requestFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("image", file.name, requestFile)
}


// 절대경로 변환
private fun getRealPathFromUri(uri: Uri, context: Context): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.let {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            filePath = it.getString(columnIndex)
        }
        it.close()
    }
    return filePath
}