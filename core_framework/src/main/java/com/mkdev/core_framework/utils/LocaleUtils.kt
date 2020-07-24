package com.mkdev.core_framework.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.mkdev.core_framework.preferences.LocalePref
import java.util.*

const val EN = "en"
const val FA = "fa"

fun localePref(): LocalePref = LocalePref.instance

fun wrapLocaledContext(context: Context, language: String): Context {
    return context.updateLocale(language)
}

fun Context.updateLocale(updatedLocale: String): Context {
    var localePref: String by localePref()
    localePref = updatedLocale

    val locale = Locale(updatedLocale)
    Locale.setDefault(locale)
    val overrideConfiguration: Configuration = resources.configuration
    overrideConfiguration.setLayoutDirection(locale)


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        overrideConfiguration.setLocale(locale)
    } else {
        overrideConfiguration.locale = locale
    }

    val updatedContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        try {
            createConfigurationContext(overrideConfiguration)
        } catch (e: Exception) {
            this@updateLocale
        }
    } else {
        this
    }

    resources.updateConfiguration(overrideConfiguration, resources.displayMetrics)
    return updatedContext

}