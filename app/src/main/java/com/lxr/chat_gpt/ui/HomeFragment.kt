package com.lxr.chat_gpt.ui

import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.addModels
import com.drake.brv.utils.setup
import com.dyne.myktdemo.base.BaseFragment
import com.lxr.chat_gpt.R
import com.lxr.chat_gpt.constants.URL
import com.lxr.chat_gpt.databinding.FragmentHomeBinding
import com.lxr.chat_gpt.entity.ChatContentReq
import com.lxr.chat_gpt.entity.ChatContentRes
import com.lxr.chat_gpt.entity.ChatMessage
import com.lxr.chat_gpt.entity.MessageReq
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.textString
import me.hgj.jetpackmvvm.ext.view.visible
import rxhttp.RxHttp
import rxhttp.awaitResult
import rxhttp.toAwait

/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2023/1/11 13:52.
 * @Description :
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    /**
     * 限制存储的上下文消息条数,多了会消耗更多重复的tokens,2/4
     */
    val contextMsg = ArrayDeque<String>(2)

    override fun initView() {
        binding.rv.setup {
            addType<ChatMessage> {
                if (isUser) {
                    R.layout.item_msg_right // 我发的消息
                } else {
                    R.layout.item_msg_left // 对方发的消息
                }
            }
        }

        binding.btnSend.clickNoRepeat {
            // 直接将输入的消息添加到列表
            val inputMessage = binding.etMessage.textString()
            addMessage2List(inputMessage, true)
            // 调取chatGpt接口
            lifecycleScope.launch {
                binding.tvTipAccepting.visible()
                sendMessage2Net(inputMessage)
                binding.tvTipAccepting.gone()
            }
        }
        binding.rv.setOnClickListener {
            KeyboardUtils.hideSoftInput(it)
        }
    }

    private suspend fun sendMessage2Net(content: String) {
        RxHttp.postBody(
            URL.COMPLETION_CHAT,
        )
            .setBody(
                ChatContentReq( // 构建请求消息体.content参数将临时存的的上下文信息和新输入的信息用换行分隔开,以便chatGpt识别
                    listOf(
                        MessageReq(content = "$contextMsg\n$content")
                    )
                )
            )
            .toAwait<ChatContentRes>()
            .awaitResult {
                val resContent = it.choices[0].message.content
                addMessage2List(resContent.trim(), false)

                // 更新上下文
                if (contextMsg.size >= 2) {
                    contextMsg.clear()
                }
                contextMsg.addLast("User: $content")
                contextMsg.addLast("Bot: $resContent")
            }.onFailure {
                ToastUtils.showShort(it.message)
            }
    }

    private fun addMessage2List(content: String, isUser: Boolean) {
        binding.rv.addModels(
            arrayListOf(
                ChatMessage(content = content, isUser = isUser)
            )
        ) // 添加一条消息
        binding.rv.scrollToPosition(binding.rv.adapter!!.itemCount - 1) // 保证最新一条消息显示
        binding.etMessage.setText("")
    }

    override fun lazyLoadData() {
    }
}
