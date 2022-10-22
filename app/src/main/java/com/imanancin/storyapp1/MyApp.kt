package com.imanancin.storyapp1

import android.app.Application
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}