package com.aoztg.greengrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_message")
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val idx: Long = 0,
    val chatId: Int,
    val type: Int = -1,
    val message: String = "",
    val nickName: String = "",
    val sentDate: String = "",
    val sentTime: String = "",
    val profileImg: String = "",
    val certId: Int = -1,
    val certImg: String = "",
)

@Entity(tableName = "unread_chat_data")
data class UnReadChatEntity(
    @PrimaryKey
    val chatId: Int = 0,
    val unReadCount: Int = 0,
    val recentChat: String = "",
    val recentChatTime: String = ""
)
