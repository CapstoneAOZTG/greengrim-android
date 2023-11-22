package com.aoztg.greengrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_message")
data class ChatEntity(
    @PrimaryKey
    val chatId: Int,
    val type: String,
    val message: String,
    val nickName: String,
    val profileImg: String
)
