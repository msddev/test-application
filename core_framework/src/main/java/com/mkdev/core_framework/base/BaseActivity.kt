package com.mkdev.core_framework.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.mkdev.core_framework.utils.localePref
import com.mkdev.core_framework.utils.updateLocale
import com.mkdev.core_framework.utils.wrapLocaledContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import java.util.*
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locale: String by localePref()
        updateLocale(locale)
    }

    override fun attachBaseContext(base: Context) {
        val locale: String by localePref()
        super.attachBaseContext(wrapLocaledContext(base, locale))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (Build.VERSION.SDK_INT in 21..25) {
            val locale: String by localePref()
            overrideConfiguration?.setLocale(Locale(locale));
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onStop() {
        super.onStop()
        job.cancelChildren()
    }
}
