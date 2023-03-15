package com.lxr.chat_gpt.entity

/**
 * 单条消息包装类
 */
data class Choice(
    val finish_reason: Any, // 估计也是分段获取字符串的一个结束表示
    val index: Int, // 如分段获取字符串结果,index可能为 0,1,2等,前端可根据此字段判断所有所有字符串结果的顺序
    val message: ChatMessage // 消息结果
)