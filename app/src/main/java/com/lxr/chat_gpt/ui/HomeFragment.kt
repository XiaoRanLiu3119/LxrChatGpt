package com.lxr.chat_gpt.ui

import android.content.res.ColorStateList
import android.text.TextUtils
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.exception.OpenAIAPIException
import com.aallam.openai.api.exception.OpenAITimeoutException
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.addModels
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.setup
import com.dyne.myktdemo.base.BaseFragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxr.chat_gpt.R
import com.lxr.chat_gpt.constants.CacheKey
import com.lxr.chat_gpt.databinding.FragmentHomeBinding
import com.lxr.chat_gpt.entity.ChatMsg
import com.lxr.chat_gpt.utils.MmkvUtil
import io.noties.markwon.Markwon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.textString
import me.hgj.jetpackmvvm.ext.view.visible
import kotlin.coroutines.cancellation.CancellationException

/**
 * @Author : Liu XiaoRan
 * @Email : 592923276@qq.com
 * @Date : on 2023/1/11 13:52.
 * @Description :
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    val conversationLimit = 8

    /**
     * 限制存储的上下文消息条数,多了会消耗更多重复的tokens,2/4
     */
    val contextMsg = ArrayDeque<String>(conversationLimit)

    var job: Job? = null
    var currentEditMsg = ""

    override fun initView() {

        if (MmkvUtil.getString(CacheKey.TOKEN).isNullOrEmpty()) {
            showApiKeyPopup()
        }
        binding.rv.setup {
            addType<ChatMsg> {
                if (role == ChatRole.User) {
                    R.layout.item_msg_right // 我发的消息
                } else {
                    R.layout.item_msg_left // 对方发的消息
                }
            }
        }

        binding.btnSend.clickNoRepeat {
            binding.etMessage.clearFocus()
            it.postDelayed({
                KeyboardUtils.hideSoftInput(it)
            }, 400)

            if (MmkvUtil.getString(CacheKey.TOKEN).isNullOrEmpty()) {
                showApiKeyPopup()
                return@clickNoRepeat
            }

            if (binding.btnSend.text == "停止") {
                job?.cancel(CancellationException())
            } else {
                // 调取chatGpt接口
                job = lifecycleScope.launch {
                    // 直接将输入的消息添加到列表
                    val inputMessage = binding.etMessage.textString()
                    if (inputMessage.isNotBlank()) {
                        addMessage2List(inputMessage, ChatRole.User)
                        getPrintResult(this, inputMessage)
                    }
                }
            }
        }
        binding.rv.setOnClickListener {
            KeyboardUtils.hideSoftInput(it)
        }
    }

    private suspend fun getPrintResult(scope: CoroutineScope, content: String) {
        // 构建参数
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId(MmkvUtil.getString(CacheKey.CHAT_MODEL).toString()),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = if (contextMsg.size != 0) "$contextMsg\n$content" else content
                )
            )
        )
        LogUtils.d("请求体::::\n${if (contextMsg.size != 0) "$contextMsg\n$content" else content}")

        println("\n>️ Creating chat completions stream...")
        // 根据请求状态更新文字和ui
        val btnSend = binding.btnSend
        val adapter = binding.rv.bindingAdapter

        OpenAI(
            config = OpenAIConfig(
                token = MmkvUtil.getString(CacheKey.TOKEN).toString(),
                host = OpenAIHost(baseUrl = MmkvUtil.getString(CacheKey.DOMAIN_URL).toString())
            )
        ).chatCompletions(chatCompletionRequest)
            .onStart {
                btnSend.text = "停止"
                btnSend.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.red))

                binding.tvTipAccepting.text = "消息接收中..."
                binding.tvTipAccepting.visible()

                currentEditMsg = ""
                addMessage2List(currentEditMsg, ChatRole.Assistant)
            }
            .onEach {
                binding.tvTipAccepting.text = "typing..."

                val contentStr = it.choices.first().delta?.content.orEmpty()
                print(contentStr)

                // 累加消息文本
                currentEditMsg += contentStr

                // 更新最新条目的文本
                val model = adapter.getModel<ChatMsg>(adapter.modelCount - 1)
                model.content = currentEditMsg
                model.notifyChange()
            }
            .onCompletion {
                btnSend.text = "发送"
                btnSend.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
                binding.tvTipAccepting.gone()

                val currentModels: List<ChatMsg> = adapter.mutable as MutableList<ChatMsg>
                val userMsg = currentModels.lastOrNull { it.role == ChatRole.User }
                val assistantMsg = currentModels.lastOrNull { it.role == ChatRole.Assistant }

                // 更新上下文
                if (contextMsg.size >= conversationLimit) {
                    contextMsg.removeFirst()
                    contextMsg.removeFirst()
                }
                contextMsg.addLast("User: ${userMsg?.content}")
                contextMsg.addLast("Bot: ${assistantMsg?.content}")

                LogUtils.d("当前上下文::::\n$contextMsg")

                if (TextUtils.isEmpty(assistantMsg?.content)) {
                    adapter.mutable.remove(assistantMsg)
                }
            }.catch {
                LogUtils.e(it.toString())
                when (it) {
                    is OpenAITimeoutException -> ToastUtils.showLong("连接超时,请检查科学上网连接状态")
                    is OpenAIAPIException -> {
                        XPopup.Builder(context)
                            .asConfirm("提示",it.message,null)
                            .show()
                    }
                    else -> ToastUtils.showLong(it.toString())
                }
            }
            .launchIn(scope)
            .join()
    }

    private fun addMessage2List(content: String, chatRole: ChatRole) {
        val chatMessage = ChatMsg(content = content, role = chatRole)
        binding.rv.addModels(
            arrayListOf(
                chatMessage
            )
        ) // 添加一条消息
        binding.rv.scrollToPosition(binding.rv.adapter!!.itemCount - 1) // 保证最新一条消息显示
        binding.etMessage.setText("")
    }

    override fun lazyLoadData() {
    }

    private fun showApiKeyPopup() {
        XPopup.Builder(context)
            .asConfirm(
                "提示",
                "尚未配置api-key,暂时无法使用聊天功能",
                "再说吧",
                "去配置",
                object : OnConfirmListener {
                    override fun onConfirm() {
                        val mainActivity = getActivity() as MainActivity
                        mainActivity.updateTab(1)
                    }
                },
                null,
                false
            )
            .show()
    }
}
