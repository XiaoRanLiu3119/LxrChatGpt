package me.hgj.jetpackmvvm.ext.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrInvisible(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 将view转为bitmap
 */
@Deprecated("use View.drawToBitmap()")
fun View.toBitmap(scale: Float = 1f, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
    if (this is ImageView) {
        if (drawable is BitmapDrawable) return (drawable as BitmapDrawable).bitmap
    }
    this.clearFocus()
    val bitmap = createBitmapSafely(
        (width * scale).toInt(),
        (height * scale).toInt(),
        config,
        1
    )
    if (bitmap != null) {
        Canvas().run {
            setBitmap(bitmap)
            save()
            drawColor(Color.WHITE)
            scale(scale, scale)
            this@toBitmap.draw(this)
            restore()
            setBitmap(null)
        }
    }
    return bitmap
}

fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
    try {
        return Bitmap.createBitmap(width, height, config)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if (retryCount > 0) {
            System.gc()
            return createBitmapSafely(width, height, config, retryCount - 1)
        }
        return null
    }
}

/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param enableRipple 点击水波纹效果开关
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(enableRipple: Boolean = false, interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        if (enableRipple) {
            it.addForegroundRipple()
        }
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}

/**
 * 点击效果前景水波纹
 */
fun View.addForegroundRipple() = with(TypedValue()) {
    if (foreground == null) { // 未设置过才设置
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, this, true)
        foreground = ContextCompat.getDrawable(context, resourceId)
    }
}
