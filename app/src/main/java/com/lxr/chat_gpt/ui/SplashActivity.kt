package com.lxr.chat_gpt.ui

import android.content.Intent
import android.os.Handler
import com.dyne.myktdemo.base.BaseActivity
import com.lxr.chat_gpt.databinding.ActivitySplashBinding
import com.lxr.chat_gpt.utils.MmkvUtil

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun initView() {
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        },500)
    }
}