package com.aoztg.greengrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat_id")
data class ChatIdEntity(
    @PrimaryKey
    val chatId: Int
)
