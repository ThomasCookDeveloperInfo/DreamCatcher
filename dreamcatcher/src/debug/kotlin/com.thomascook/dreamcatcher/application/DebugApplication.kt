package com.thomascook.dreamcatcher.application

import android.os.StrictMode
import com.thomascook.core.security.SecureKeyStore
import com.thomascook.dreamcatcher.EventReporter
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern

/**
 * Android application class for debug builds
 */
private const val TAG = "DebugApplication"

//Value copied over from Environment class
private const val REALM_DB_KEY_ALIAS = "realmDb"

class DebugApplication : android.support.multidex.MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        Environment.initialise(this)

        //Enable strict mode
        enableStrictMode()

        //Install Facebook Stetho - do it on the background thread so strict mode is not violated
        Environment.get().map { environment ->
            //Get realm encryption key
            val realmKey = SecureKeyStore.getKey(applicationContext, REALM_DB_KEY_ALIAS)
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    RealmInspectorModulesProvider.builder(this)
                                            .withDefaultEncryptionKey(realmKey + realmKey)
                                            .withMetaTables()
                                            .withDescendingOrder()
                                            .withLimit(1000)
                                            .databaseNamePattern(Pattern.compile(".+\\.realm"))
                                            .build())
                            .build())

            environment
        }.subscribeOn(Schedulers.io())
                .subscribe(
                        { _ ->
                            EventReporter.i(TAG, "Stetho initialised.")
                        },
                        { error ->
                            EventReporter.d(TAG, "Failed to initialise Stetho", error)
                        })
    }

    private fun enableStrictMode() {
        //Add support for string mode in debug code
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyDeath()
                .build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                //.penaltyDeath()
                .build())
    }
}