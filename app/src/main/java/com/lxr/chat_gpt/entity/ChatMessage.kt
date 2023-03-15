package com.lxr.chat_gpt.entity

/**
 * 消息类,目前列表和解析用的这同一个实体
 */
data class ChatMessage(
    val content: String, // 消息内容
    val isUser: Boolean = false, // 自己加的判断,是用户还是chatGpt返回
    val role: String = "user" // 用户user.chatGpt返回assistant,还有一个用于对话开始的,好像关于什么个性化对话的
)