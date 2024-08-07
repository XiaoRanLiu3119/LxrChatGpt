package com.lxr.chat_gpt.utils.view

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.blankj.utilcode.util.LogUtils
import io.noties.markwon.Markwon

class BindingAdapters {
    @BindingAdapter("richText")
    fun setRichText(view: TextView, richText: String) {
        val markwon = Markwon.create(view.context)
        markwon.setMarkdown(view,richText)
    }
}