package com.tzeench.treeprotectormobile.android

import android.app.Application
import com.tzeench.treeprotectormobile.di.injectionModule
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.KoinAppDeclaration

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val appDeclaration: KoinAppDeclaration = {
            androidLogger(level = if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(androidContext = this@App)
        }
        startKoin {
            appDeclaration()
            modules(injectionModule(enableNetworkLogs = true))
        }

    }
}