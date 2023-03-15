package com.lxr.chat_gpt.ui

import android.content.Intent
import android.os.Handler
import com.dyne.myktdemo.base.BaseActivity
import com.lxr.chat_gpt.databinding.ActivitySplashBinding
import com.lxr.chat_gpt.utils.MmkvUtil

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun initView() {
        Handler().postDelayed({
            if (MmkvUtil.getString("token").isNullOrEmpty()){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            } else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        },1200)
    }
}