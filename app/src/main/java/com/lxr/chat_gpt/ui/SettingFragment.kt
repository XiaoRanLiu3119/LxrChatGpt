package com.lxr.chat_gpt.ui

import com.blankj.utilcode.util.ToastUtils
import com.dyne.mj.utils.Utils
import com.dyne.myktdemo.base.BaseFragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.OnInputConfirmListener
import com.lxr.chat_gpt.constants.CacheKey
import com.lxr.chat_gpt.databinding.FragmentSettingBinding
import com.lxr.chat_gpt.utils.MmkvUtil
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat

/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2023/1/11 13:52.
 * @Description :
 */
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override fun initView() {
        binding.rlApiKey.clickNoRepeat {
            XPopup.Builder(this.context)
                .asInputConfirm("提示","","请输入sk-开头的api-key",object :OnInputConfirmListener{
                    override fun onConfirm(text: String?) {
                        when {
                            text.isNullOrBlank() -> {
                                ToastUtils.showShort("不可为空,请输入")
                            }
                            !text.startsWith("sk-") -> {
                                ToastUtils.showShort("格式不对,sk-开头的")
                            }
                            else -> {
                                ToastUtils.showShort("成功")
                                MmkvUtil.put(CacheKey.TOKEN, text)
                                val mainActivity = getActivity() as MainActivity
                                mainActivity.updateTab(0)
                            }
                        }
                    }
                })
                .show()
        }

        binding.rlClearCache.clickNoRepeat {
            XPopup.Builder(this.context)
                .asConfirm(
                    "提示",
                    "清除后需重新录入api-key,确认清除?",
                    object : OnConfirmListener {
                        override fun onConfirm() {
                            MmkvUtil.clearAll()
                        }
                    }
                )
                .show()
        }
    }

    override fun lazyLoadData() {
    }
}
