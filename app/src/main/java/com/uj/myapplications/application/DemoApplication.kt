package com.uj.myapplications.application

import android.app.Application
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.uj.myapplications.BuildConfig
import com.uj.myapplications.utility.Config
import timber.log.Timber


class DemoApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        // Intialize
        FirebaseApp.initializeApp(applicationContext)
        Config.attachContext(applicationContext)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}