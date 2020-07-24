package com.mkdev.testapplication

import android.app.Application
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
    }

    // CONFIGURATION ---
    open fun configureDi() {
        startKoin {
            androidContext(this@App)
            modules(provideApplicationModules())
            logger(if (BuildConfig.DEBUG) AndroidLogger(Level.DEBUG) else EmptyLogger())
        }
    }

    // PUBLIC API ---
    open fun provideApplicationModules() = koinApplicationModules
}