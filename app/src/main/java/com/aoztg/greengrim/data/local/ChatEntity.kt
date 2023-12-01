package com.aoztg.greengrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_message")
data class ChatEntity(
    val chatId: Int,
    val type: Int = -1,
    val message: String = "",
    val nickName: String = "",
    val sentDate: String = "",
    val sentTime: String = "",
    val profileImg: String = "",
    val certId: Int = -1,
    val certImg: String = "",
){
    @PrimaryKey(autoGenerate = true)
    var idx: Long = 0
}
