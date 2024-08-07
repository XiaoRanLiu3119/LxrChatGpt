package com.lxr.chat_gpt.entity

import androidx.databinding.BaseObservable
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatRole

/**
 * @Author      : Liu XiaoRan
 * @Email       : 592923276@qq.com
 * @Date        : on 2023/3/16 15:45.
 * @Description :
 */
data class ChatMsg(val role: ChatRole, var content: String) : BaseObservable()
