package com.thomascook.dreamcatcher.application

import android.content.Context
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.thomascook.core.security.SecureKeyStore
import com.thomascook.dreamcatcher.EventReporter
import com.thomascook.dreamcatcher.application.storage.AndroidRealmSchedulers
import com.thomascook.dreamcatcher.BuildConfig
import io.fabric.sdk.android.Fabric
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.atomic.AtomicReference

/**
 * Used to create and initialise application environment
 */
private val ERROR_ENVIRONMENT_INITIALISATION =
        Observable.error<Environment>(IllegalStateException("Environment hasn't been initialised"))

//Atomic reference for holding
private val environmentRef: AtomicReference<Observable<Environment>> = AtomicReference()

//Key name alias for realm DB
private const val REALM_DB_KEY_ALIAS = "realmDb"
private const val TAG = "Environment"

class Environment private constructor(context: Context) {

    //Property to access application preferences
    val preferences = Preferences(context)

    companion object {

        fun initialise(context: Context) {

            val appContext = context.applicationContext

            val environmentObservable = Observable.fromCallable<Environment> {
                try {
                    //Init crashlytics
                    initialiseCrashlytics(appContext)

                    //Create environment
                    Environment(appContext)
                } catch (error: Exception) {
                    EventReporter.e(TAG, "Failed to create environment", error)
                    environmentRef.compareAndSet(null,
                            Observable.error(IllegalStateException("Failed to create environment", error)))
                    throw error
                }
            }.subscribeOn(Schedulers.io())
                    .map { environment ->

                        try {
                            val key = if (!SecureKeyStore.hasKey(context, REALM_DB_KEY_ALIAS)) {
                                SecureKeyStore.createKey(context, REALM_DB_KEY_ALIAS)
                            } else {
                                SecureKeyStore.getKey(context, REALM_DB_KEY_ALIAS)
                            }

                            Realm.init(appContext)
                            Realm.setDefaultConfiguration(
                                    RealmConfiguration.Builder()
                                            .encryptionKey(key + key)
                                            .build())

                            //Initialisation has finished - set new observable
                            environmentRef.set(Observable.just(environment))

                            //Return Environment
                            environment
                        } catch (error: Throwable) {
                            EventReporter.e(TAG, "Failed to initialise realm", error)
                            environmentRef.compareAndSet(null,
                                    Observable.error(IllegalStateException("Failed to initialise realm", error)))
                            throw error
                        }
                    }.subscribeOn(AndroidRealmSchedulers.realmThread())
                    .share().cache()

            environmentRef.compareAndSet(null, environmentObservable)
        }

        fun get(): Observable<Environment> {
            return environmentRef.get() ?: return ERROR_ENVIRONMENT_INITIALISATION
        }

        //Used to initialise fabric kits
        private fun initialiseCrashlytics(context: Context) {
            Fabric.with(context, Crashlytics.Builder()
                    .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                    .build())
        }
    }
}