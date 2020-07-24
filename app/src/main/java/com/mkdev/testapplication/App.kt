package com.mkdev.testapplication

import android.app.Application
import com.mkdev.core_framework.preferences.PrefDelegate
import com.mkdev.core_framework.utils.localePref
import com.mkdev.core_framework.utils.wrapLocaledContext
import com.mkdev.testapplication.di.koinApplicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import org.koin.core.logger.Level

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()

        PrefDelegate.init(this)
        configureDefaultLanguage()

    }

    // CONFIGURATION ---
    open fun configureDi() {
        startKoin {
            androidContext(this@App)
            modules(provideApplicationModules())
            logger(if (BuildConfig.DEBUG) AndroidLogger(Level.DEBUG) else EmptyLogger())
        }
    }

    private fun configureDefaultLanguage() {
        val currentLocale: String by localePref()
        wrapLocaledContext(this, currentLocale)
    }

    // PUBLIC API ---
    open fun provideApplicationModules() = koinApplicationModules
}