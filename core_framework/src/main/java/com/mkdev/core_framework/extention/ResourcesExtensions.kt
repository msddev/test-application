package com.mkdev.core_framework.extention

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes

fun TypedArray.getDrawableCompat(context: Context, resource: Int): Drawable? {
    return getResourceId(resource, 0).takeIf { it > 0 }?.let { context.getDrawableCompat(it) }
}

/**
 * calculates the height of statusBar
 */
fun Resources.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getColorFromTheme(@AttrRes attrRes: Int): Int = theme.getColorFromAttr(attrRes)

fun Resources.Theme.getColorFromAttr(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()