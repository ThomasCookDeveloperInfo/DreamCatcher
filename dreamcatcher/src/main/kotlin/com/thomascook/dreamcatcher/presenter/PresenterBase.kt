package com.thomascook.dreamcatcher.presenter

import io.reactivex.disposables.CompositeDisposable
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.TiView

/**
 * Base class for presenters
 */
abstract class PresenterBase<V : TiView> : TiPresenter<V>() {
    //Used to clean up pending subscriptions/disposables
    protected val disposables = CompositeDisposable()

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}