package com.aoztg.greengrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_message")
data class ChatEntity(
    val chatId: Int,
    val type: Int,
    val message: String,
    val nickName: String,
    val profileImg: String,
    val certId: Int,
    val certImg: String,
    val sentDate: String,
    val sentTime: String
){
    @PrimaryKey(autoGenerate = true)
    var idx: Long = 0
}
