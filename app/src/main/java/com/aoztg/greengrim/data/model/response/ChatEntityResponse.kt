package com.aoztg.greengrim.data.model.response

import com.aoztg.greengrim.data.local.ChatEntity

data class ChatEntityResponse(
    val hasNext: Boolean,
    val chatEntityList: List<ChatEntity>
)
