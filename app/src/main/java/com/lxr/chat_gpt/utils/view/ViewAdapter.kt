package com.lxr.chat_gpt.utils.view

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.noties.markwon.Markwon

object ViewAdapter {

    private var markdown:Markwon? = null

    @BindingAdapter(value = ["richText"], requireAll = false)
    @JvmStatic
    fun setRichText(textView: TextView, richText: String?) {
        if (markdown == null){
            markdown = Markwon.create(textView.context)
        }
        markdown?.setMarkdown(textView, richText!!)
    }

}