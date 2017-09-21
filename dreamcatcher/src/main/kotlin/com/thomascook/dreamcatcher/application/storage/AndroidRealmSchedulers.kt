package com.thomascook.dreamcatcher.application.storage

import android.os.HandlerThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.atomic.AtomicReference

/**
 * Realm scheduler used to access realm data using rx.
 */
private val INSTANCE = AtomicReference<AndroidRealmSchedulers>()

class AndroidRealmSchedulers private constructor() {
    private val handlerThread: HandlerThread = HandlerThread("REALM")
    private var mainThreadScheduler: Scheduler

    init {
        handlerThread.start()
        mainThreadScheduler = AndroidSchedulers.from(handlerThread.looper)
    }

    private fun dispose() {
        handlerThread.quit()
    }

    companion object {
        private fun getInstance(): AndroidRealmSchedulers {
            while (true) {
                var current = INSTANCE.get()
                if (current != null) {
                    return current
                }

                current = AndroidRealmSchedulers()

                if (INSTANCE.compareAndSet(null, current)) {
                    return current
                } else {
                    current.dispose()
                }
            }
        }

        /**
         * A [Scheduler] which executes actions on the Android UI realmThread.
         */
        fun realmThread(): Scheduler = getInstance().mainThreadScheduler
    }
}