package com.thomascook.dreamcatcher.application

/**
 * Android application class for release builds
 */
class ReleaseApplication : android.support.multidex.MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Environment.initialise(this)
    }
}