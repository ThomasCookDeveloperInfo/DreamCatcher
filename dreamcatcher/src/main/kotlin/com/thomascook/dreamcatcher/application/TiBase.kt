package com.thomascook.dreamcatcher.application

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import net.grandcentrix.thirtyinch.TiFragment
import net.grandcentrix.thirtyinch.TiPresenter
import net.grandcentrix.thirtyinch.TiView

private const val KEY_STATE_BUNDLE = "state_bundle"

/**
 * Base class for fragments that supports TI framework
 */
abstract class TiFragmentBase<P : TiPresenter<V>, V : TiView> : TiFragment<P, V>() {
    //We store our intermediate state here
    protected lateinit var mStateBundle: Bundle
    //Used to clean up pending subscriptions/viewDisposables
    protected val viewDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStateBundle = savedInstanceState?.getBundle(KEY_STATE_BUNDLE) ?: Bundle()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        //Save the state of the fragment
        outState?.putBundle(KEY_STATE_BUNDLE, mStateBundle)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        this.viewDisposables.clear()
        super.onDestroyView()
    }
}

/**
 * Dummy presenter for fragments that don't have their own presenter
 */
class TiPresenterHolder : TiPresenter<TiView>()