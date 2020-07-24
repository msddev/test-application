package com.mkdev.core_framework.utils

import android.view.View

class SafeViewClickListener(private val clickListener: ((view: View) -> Unit)?) :
    View.OnClickListener {
    private var isEnabled = true
    override fun onClick(v: View) {
        if (isEnabled) {
            isEnabled = false
            clickListener?.invoke(v)
            v.postDelayed(Runnable {
                isEnabled = true
            }, 500)
        }
    }
}

fun View.setSafeOnClickListener(clickListener: ((view: View) -> Unit)?) {
    setOnClickListener(SafeViewClickListener(clickListener))
}