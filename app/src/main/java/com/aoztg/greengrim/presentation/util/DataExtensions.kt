package com.aoztg.greengrim.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.aoztg.greengrim.presentation.ui.challenge.model.CategoryName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


internal fun Uri.toMultiPart(context: Context): MultipartBody.Part {
    val file = File(getRealPathFromUri(this, context) ?: "")
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

internal fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
}

internal fun LocalDate.toHeaderText(): String {

    val dayOfWeekHash = hashMapOf(
        "MONDAY" to "월요일",
        "TUESDAY" to "화요일",
        "WEDNESDAY" to "수요일",
        "THURSDAY" to "목요일",
        "FRIDAY" to "금요일",
        "SATURDAY" to "토요일",
        "SUNDAY" to "일요일",
    )

    return this.monthValue.toString() + "월 " + this.dayOfMonth + "일, " + dayOfWeekHash[this.dayOfWeek.toString()]
}

internal fun String.toCategoryText(): String{
    CategoryName.values().forEach {
        if(it.value == this){
            return it.text
        }
    }
    return ""
}

internal fun YearMonth.toText() = year.toString() + "년 " + monthValue + "월"