package com.lxr.chat_gpt

import android.app.Application
import com.drake.brv.utils.BRV
import com.lxj.xpopup.XPopup
import com.lxr.chat_gpt.constants.CacheKey
import com.lxr.chat_gpt.constants.Config
import com.lxr.chat_gpt.utils.MmkvUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSizeConfig
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.ssl.HttpsUtils
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


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
        // XPopup配置,主要的颜色,确认按钮文字的颜色/控件选中的颜色等
        XPopup.setPrimaryColor(R.color.colorPrimary)
        initNet()
    }


    private fun initNet(){
        val sslParams = HttpsUtils.getSslSocketFactory()
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true }) //忽略host验证
            .build()

        RxHttpPlugins.init(client)
            .setDebug(BuildConfig.DEBUG,true,2)  //调试模式/分段打印/json数据格式化输出
            .setOnParamAssembly {
                it.addHeader("Authorization", MmkvUtil.getString(CacheKey.TOKEN))
            }
    }

}
