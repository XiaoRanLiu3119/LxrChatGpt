package com.lxr.chat_gpt.entity


data class MessageReq(
    val content: String,
    val role: String = "user"
)