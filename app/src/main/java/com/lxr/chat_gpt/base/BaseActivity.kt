package com.dyne.myktdemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ktx.fitsTitleBar
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.lxr.chat_gpt.constants.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(), OnTitleBarListener {
    val TAG = javaClass.name
    protected lateinit var binding: T

    protected var loadingPopup: LoadingPopupView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initBindingViewRoot())

        EventBus.getDefault().register(this)
        initTitleStatusBar()
        initView()
        initListener()
    }

    /**
     * 初始化viewBinding返回根布局
     */
    private fun initBindingViewRoot(): View {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as T
        return binding.root
    }

    /**
     * 初始化布局
     */
    abstract fun initView()

    /**
     * 初始化监听器
     */
    protected fun initListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(event: MessageEvent) {

    }

    /**
     * 初始化标题栏和沉浸式状态栏
     */
    private fun initTitleStatusBar(){
        val findTitleBar = findTitleBar(findViewById(Window.ID_ANDROID_CONTENT))
        findTitleBar?.setOnTitleBarListener(this)

        immersionBar {
            statusBarDarkFont(true)
            if (findTitleBar != null) {
                fitsTitleBar(findTitleBar)
            }
        }
    }

    override fun onLeftClick(titleBar: TitleBar?) {
        super.onLeftClick(titleBar)
        onBackPressed()
    }

    override fun onRightClick(titleBar: TitleBar?) {
        super.onRightClick(titleBar)
    }

    override fun onTitleClick(titleBar: TitleBar?) {
        super.onTitleClick(titleBar)
    }

    private fun findTitleBar(group: ViewGroup): TitleBar? {
        for (i in 0 until group.childCount) {
            val view = group.getChildAt(i)
            if (view is TitleBar) {
                return view
            } else if (view is ViewGroup) {
                val titleBar = findTitleBar(view)
                if (titleBar != null) {
                    return titleBar
                }
            }
        }
        return null
    }

    /**
     * 打开等待框
     */
    protected fun showLoading() {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .isLightNavigationBar(true)
                .hasShadowBg(false)
                .asLoading()
                .show() as LoadingPopupView
        }
        loadingPopup?.show()
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        loadingPopup?.dismiss()
    }

}