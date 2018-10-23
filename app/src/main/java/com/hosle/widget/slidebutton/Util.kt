package com.hosle.widget.slidebutton

import android.app.Activity
import android.content.Context

/**
 * Created by tanjiahao on 2018/10/23
 * Original Project HoSlideButton
 */
fun Context.dp2px(dpValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}