package com.aoztg.greengrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "unread_chat_data")
data class UnReadChatEntity(
    @PrimaryKey
    val chatId: Int = 0,
    val unReadCount: Int = 0,
    val recentChat: String = "",
    val recentChatTime: String = "",
    val recentChatDate: String = ""
)
