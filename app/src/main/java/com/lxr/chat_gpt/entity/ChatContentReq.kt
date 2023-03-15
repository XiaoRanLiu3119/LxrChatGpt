package com.lxr.chat_gpt.entity

data class ChatContentReq(
    val messages: List<MessageReq>,
    val model: String = "gpt-3.5-turbo"
)