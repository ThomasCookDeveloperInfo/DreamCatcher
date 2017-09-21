package com.thomascook.dreamcatcher.application.home

import android.content.Context
import com.thomascook.dreamcatcher.application.InjectorProvider
import com.thomascook.dreamcatcher.presenter.HomePresenter
import com.thomascook.dreamcatcher.presenter.HomeView

/**
 * Presenter used by the [HomeActivity]
 */
class AndroidHomePresenter(private val context: Context,
                           injectorProvider: InjectorProvider) : HomePresenter() {

    override fun onAttachView(attachedView: HomeView) {
        super.onAttachView(attachedView)
    }
}