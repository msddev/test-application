package com.mkdev.core_framework.extention

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.text.Html
import android.text.InputFilter
import android.text.Spanned
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visibleBy(condition: Boolean) {
    if (condition)
        visible()
    else
        gone()
}

fun androidx.recyclerview.widget.LinearLayoutManager.hasReachedTheEnd(
    recyclerView: androidx.recyclerview.widget.RecyclerView,
    minimumDistanceToConsider: Int = 0
): Boolean {
    recyclerView.adapter?.let {
        val latestAvailablePosition = itemCount - 1 - minimumDistanceToConsider
        val lastVisiblePosition = findLastVisibleItemPosition()
        return itemCount > 0 && (lastVisiblePosition >= latestAvailablePosition)
    }
    return false
}

inline fun androidx.recyclerview.widget.RecyclerView.addLoadMoreForLinear(crossinline action: () -> Unit) {
    addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

        override fun onScrolled(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            dx: Int,
            dy: Int
        ) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager =
                recyclerView?.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            if (layoutManager.hasReachedTheEnd(recyclerView)) {
                action()
            }
        }
    })
}

inline fun NestedScrollView.addLoadMoreForLinear(
    bottomCutOff: Int = 10.dpToPixel,
    crossinline action: () -> Unit
) {
    setOnScrollChangeListener(
        NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            val diff = getBottom() - (getHeight() + scrollY)

            if (diff < bottomCutOff) {
                action()
            }
            /*    if (scrollY - bottomCutOff == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    action()
                }*/
        })
}

fun Int.dpToPx(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        displayMetrics
    )
}

fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
    var drawable = ContextCompat.getDrawable(this, drawableId)
    return drawable?.let {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(it).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            it.intrinsicWidth, it.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        it.setBounds(0, 0, canvas.width, canvas.height)
        it.draw(canvas)
        bitmap
    } ?: run {
        null
    }
}

@Suppress("DEPRECATION")
fun String.asHtmlFormat(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

val Int.dpToPixel: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

fun TextView.setMaxLength(length: Int) {
    filters = arrayOf(InputFilter.LengthFilter(length))
}