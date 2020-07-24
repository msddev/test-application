package com.mkdev.core_framework.extention

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.AudioManager.RINGER_MODE_SILENT
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat

/***
 * returns the intent if the system can handle the intent by some activity or null otherwise
 */
fun Intent.hasActivity(context: Context): Intent? {
    return if (resolveActivity(context.packageManager) != null) {
        this
    } else
        null
}


fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableRes)
}

fun Context.isVibrateEnabled(): Boolean {
    return (getSystemService(AUDIO_SERVICE) as AudioManager).ringerMode != RINGER_MODE_SILENT
}