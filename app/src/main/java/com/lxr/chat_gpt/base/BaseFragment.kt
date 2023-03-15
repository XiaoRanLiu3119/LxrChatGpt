package com.dyne.myktdemo.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ktx.fitsTitleBar
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<T : ViewBinding> : Fragment(), OnTitleBarListener {
    val TAG: String = javaClass.name

    lateinit var activity: AppCompatActivity
    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    // 是否第一次加载
    private var isFirstLoad: Boolean = true
    protected var loadingPopup: LoadingPopupView? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return initBindingViewRoot(container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirstLoad = true
        initView()
        initTitleStatusBar()
        initData()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        handler.removeCallbacksAndMessages(null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(simpleMessage: String?) {
    }

    /**
     * 初始化viewBinding返回根布局
     */
    private fun initBindingViewRoot(container: ViewGroup?): View {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = method.invoke(null, layoutInflater, container, false) as T
        return binding.root
    }

    /**
     * 初始化布局
     */
    protected open fun initView() {
    }

    /**
     * Fragment执行onCreate后触发的方法
     */
    protected fun initData() {
    }

    /**
     * 初始化监听器
     */
    protected fun initListener() {
    }

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirstLoad) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()
                isFirstLoad = false
            }, lazyLoadTime())
        }
    }

    private fun initTitleStatusBar() {
        val findTitleBar = findTitleBar((view as ViewGroup?)!!)
        if (findTitleBar is TitleBar) {
            findTitleBar.setOnTitleBarListener(this)
        }

        immersionBar {
            if (findTitleBar != null) { // 沉浸式适配出标题栏的颜色
                fitsTitleBar(findTitleBar)
            }
        }
    }

    /**
     * 打开等待框
     */
    protected fun showLoading(message: String = "加载中...") {
        if (loadingPopup == null) {
            loadingPopup = XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .isLightNavigationBar(true)
                .hasShadowBg(false)
                .asLoading()
        }
        loadingPopup?.setTitle(message)
        loadingPopup?.show() as LoadingPopupView
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        loadingPopup?.dismiss()
    }

    /**
     * 遍历出标题栏
     */
    private fun findTitleBar(group: ViewGroup): View? {
        for (i in 0 until group.childCount) {
            val view = group.getChildAt(i)
            if (view is TitleBar || view is Toolbar) {
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

    override fun onLeftClick(titleBar: TitleBar?) {
        super.onLeftClick(titleBar)
    }

    override fun onRightClick(titleBar: TitleBar?) {
        super.onRightClick(titleBar)
    }

    override fun onTitleClick(titleBar: TitleBar?) {
        super.onTitleClick(titleBar)
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    open fun lazyLoadTime(): Long {
        return 300
    }
}
