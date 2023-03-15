package com.lxr.chat_gpt.entity

/**
 * 请求结果的解析类
 */
data class ChatContentRes(
    val choices: List<Choice>, // 消息结果集合,包装的对象带下标
    val id: String,
    val usage: Usage // 看起来是统计对话tokens消耗量等
)