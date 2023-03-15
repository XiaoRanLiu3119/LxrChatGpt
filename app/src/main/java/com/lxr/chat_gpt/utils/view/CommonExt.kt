package com.lxr.chat_gpt.utils.view

/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2023/2/10 11:36.
 * @Description :
 */

/**
 * 空和非空操作
 */
fun Any?.notNull(notNullAction: (value: Any) -> Unit, nullAction1: () -> Unit) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction1.invoke()
    }
}
