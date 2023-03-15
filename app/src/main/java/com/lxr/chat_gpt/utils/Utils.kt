package com.dyne.mj.utils

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxr.chat_gpt.MyApp
import com.lxr.chat_gpt.constants.CacheKey
import com.lxr.chat_gpt.ui.LoginActivity
import com.lxr.chat_gpt.utils.MmkvUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author      : Liu XiaoRan
 * @Email       : 592923276@qq.com
 * @Date        : on 2022/6/11 13:41.
 * @Description :
 */
object Utils {
    /**
     * 退出登录
     */
    fun logout() {
        // 清除当前系统的token
        MmkvUtil.put(CacheKey.TOKEN, "")
        val intent = Intent(MyApp.instance, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        MyApp.instance.startActivity(intent)
    }

    /**
     * 拨打电话
     */
    fun callPhone(phoneNum: String?) {
        if (phoneNum.isNullOrBlank()) {
            ToastUtils.showShort("电话号为空")
            return
        }
        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:$phoneNum")
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        MyApp.instance.startActivity(intent)
    }


    /**
     * 获取某个日期的前几天的日期
     *
     * @param date      某日期
     * @param dayNumber 后面第几天 默认 0 即返回date的当天
     * @return
     */
    fun getBeforeDay(date: Date?, dayNumber: Int = 0): String? {
        val c = Calendar.getInstance()
        c.time = date
        val day = c[Calendar.DATE]
        c[Calendar.DATE] = day - dayNumber
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.time)
    }

    /**
     * 开始在结束日期之后了,用于校验开始结束日期选择
     */
    fun startAfterEndDate(startDateStr: String?, endDateStr: String): Boolean {
        if (endDateStr == "长期") {
            return false
        }
        val startDate = TimeUtils.string2Date(startDateStr, "yyyy-MM-dd")
        val endDate = TimeUtils.string2Date(endDateStr, "yyyy-MM-dd")
        return startDate.after(endDate)
    }
}