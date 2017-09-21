package com.thomascook.dreamcatcher.application.startup

import android.content.Context
import com.thomascook.dreamcatcher.application.InjectorProvider
import com.thomascook.dreamcatcher.presenter.StartupPresenter
import com.thomascook.dreamcatcher.presenter.StartupView

private const val TAG = "AndroidStartupPresenter"

class AndroidStartupPresenter(private val context: Context,
                              injectorProvider: InjectorProvider) : StartupPresenter() {

    override fun onAttachView(view: StartupView) {
        super.onAttachView(view)
    }
}