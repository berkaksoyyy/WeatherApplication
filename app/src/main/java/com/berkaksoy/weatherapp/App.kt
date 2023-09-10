package com.berkaksoy.weatherapp

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.berkaksoy.weatherapp.utils.FilesUtils
import dagger.hilt.android.HiltAndroidApp
import jonathanfinerty.once.Once
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        Once.initialise(this)
        initCheckCache()
    }

    private fun initCheckCache() {
        try {
            if (!Once.beenDone(TimeUnit.MINUTES, 30, "ClearingCache")) {
                Once.markDone("ClearingCache")
                Thread {
                    FilesUtils.deleteDirectoryTree(
                        applicationContext.cacheDir
                    )
                }.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}