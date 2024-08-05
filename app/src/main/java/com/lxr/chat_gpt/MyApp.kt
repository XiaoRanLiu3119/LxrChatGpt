package com.lxr.chat_gpt

import android.app.Application
import com.drake.brv.utils.BRV
import com.lxj.xpopup.XPopup
import com.lxr.chat_gpt.constants.CacheKey
import com.lxr.chat_gpt.utils.MmkvUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import me.jessyan.autosize.AutoSizeConfig
import java.util.concurrent.TimeUnit


/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2023/1/9 09:30.
 * @Description :
 */
class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 缓存mmkv
        MMKV.initialize(this)
        // 上拉刷新下来加载样式
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { _, _ -> MaterialHeader(this) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { _, _ -> ClassicsFooter(this) }
        // brv使用,初始化BindingAdapter的默认绑定ID, 如果不使用DataBinding并不需要初始化,m为dataBinding中item布局的variable   name="m"
        BRV.modelId = BR.m
        // AutoSize配置,不想让 App 内的字体大小跟随系统设置中字体大小的改变
        AutoSizeConfig.getInstance().isExcludeFontScale = true
        initNet()
        initDefaultCacheConfig()
    }

    private fun initDefaultCacheConfig(){
        if (!MmkvUtil.hasKey(CacheKey.DOMAIN_URL)) MmkvUtil.put(CacheKey.DOMAIN_URL,"https://api.chatanywhere.tech")
        if (!MmkvUtil.hasKey(CacheKey.CHAT_MODEL)) MmkvUtil.put(CacheKey.CHAT_MODEL,"gpt-3.5-turbo")
    }

    private fun initNet() {
        HttpClient(OkHttp) {
            engine {
                // this: OkHttpConfig
                config {
                    // this: OkHttpClient.Builder
                    followRedirects(true)
                    connectTimeout(1,TimeUnit.MINUTES)
                    callTimeout(1,TimeUnit.MINUTES)
                    readTimeout(1,TimeUnit.MINUTES)
                }
            }
        }
    }

}
