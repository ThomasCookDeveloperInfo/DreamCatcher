package com.thomascook.dreamcatcher

import android.util.Log
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Logger that uses Crashlytics to log errors
 */
object EventReporter {

    fun e(tag: String, message: String, e: Throwable?) {
        if (Fabric.isInitialized()) {
            Crashlytics.log(Log.ERROR, tag, message)
            Crashlytics.logException(e)
        } else {
            Log.e(tag, message, e)
        }
    }

    fun e(tag: String, message: String) {
        if (Fabric.isInitialized()) {
            Crashlytics.log(Log.ERROR, tag, message)
        } else {
            Log.e(tag, message)
        }
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun d(tag: String, message: String, e: Throwable?) {
        Log.d(tag, message, e)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

}