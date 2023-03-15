package com.lxr.chat_gpt.constants

import rxhttp.wrapper.annotation.DefaultDomain

/**
 * @Author      : xia chuanqi
 * @Email       : 751528989@qq.com
 * @Date        : on 2022/6/11 10:55.
 * @Description :通用接口
 */

object URL {

    /**
     * 全局BASE_URL
     */
    @DefaultDomain
    const val BASE_URL = "https://api.openai.com/"

    /**
     * 聊天
     */
    const val COMPLETION_CHAT = "/v1/chat/completions"

    /**
     * 创建图片
     */
    const val IMAGES_GENERATION = "/v1/images/generations"


}