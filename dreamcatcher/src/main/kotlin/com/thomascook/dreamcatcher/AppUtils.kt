package com.thomascook.dreamcatcher

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Random collection of helper functions
 */
object AppUtils {
    /**
     * Function for removed un-needed disposable from the queue of pending disposables.
     */
    fun removeDisposable(compositeDisposable: CompositeDisposable, disposable: Disposable?) =
            if (disposable != null)
                compositeDisposable.remove(disposable)
            else false

    /**
     * Function for adding disposable to the queue of pending disposables.
     */
    fun addDisposable(compositeDisposable: CompositeDisposable, disposable: Disposable?) =
            if (disposable != null) compositeDisposable.add(disposable) else false
}