package com.lxr.chat_gpt.ui

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.View.OnClickListener
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.ToastUtils
import com.dyne.myktdemo.base.BaseActivity
import com.lxr.chat_gpt.R
import com.lxr.chat_gpt.constants.CacheKey
import com.lxr.chat_gpt.constants.Constants
import com.lxr.chat_gpt.constants.URL
import com.lxr.chat_gpt.databinding.ActivityLoginBinding
import com.lxr.chat_gpt.utils.MmkvUtil
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat
import me.hgj.jetpackmvvm.ext.view.isTrimEmpty
import me.hgj.jetpackmvvm.ext.view.textStringTrim

/**
 * 选择系统后的登录页面
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun initView() {
        binding.tvVersion.text =
            if (URL.BASE_URL.contains("//test")) {
                "Version ${AppUtils.getAppVersionName()} Dev"
            } else {
                "Version ${AppUtils.getAppVersionName()} Pro"
            }

        SpanUtils
            .with(binding.tvApikeyAddressLink)
            .append("查看api-key")
            .appendLine()
            .append(Constants.OPENAI_API_KEYS_ADDRESS)
            .setClickSpan(
                ColorUtils.getColor(R.color.blue),
                true
            ) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(Constants.OPENAI_API_KEYS_ADDRESS))
                startActivity(intent)
            }
            .create()

        binding.mbLogin.clickNoRepeat {
            when {
                binding.etAcc.isTrimEmpty() -> {
                    ToastUtils.showShort("请输入apiKey")
                    return@clickNoRepeat
                }
            }
            MmkvUtil.put(CacheKey.TOKEN, "Bearer ${binding.etAcc.textStringTrim()}")
            ActivityUtils.startActivity(MainActivity::class.java)
            finish()
        }
    }
}
