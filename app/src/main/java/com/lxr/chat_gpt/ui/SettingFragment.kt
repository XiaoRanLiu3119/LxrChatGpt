package com.lxr.chat_gpt.ui

import com.dyne.mj.utils.Utils
import com.dyne.myktdemo.base.BaseFragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxr.chat_gpt.databinding.FragmentSettingBinding
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat

/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2023/1/11 13:52.
 * @Description :
 */
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override fun initView() {

        binding.rlLogout.clickNoRepeat {
            XPopup.Builder(this.context)
                .asConfirm(
                    "提示",
                    "确认退出吗?",
                    object : OnConfirmListener {
                        override fun onConfirm() {
                            Utils.logout()
                        }
                    }
                )
                .show()
        }
    }

    override fun lazyLoadData() {
    }
}
