package com.thomascook.dreamcatcher.application.startup

import android.content.Context
import com.thomascook.dreamcatcher.application.InjectorProvider
import com.thomascook.dreamcatcher.presenter.StartupPresenter
import com.thomascook.dreamcatcher.presenter.StartupView

private const val TAG = "AndroidStartupPresenter"

class AndroidStartupPresenter(private val context: Context,
                              injectorProvider: InjectorProvider) : StartupPresenter() {

    private var animating = false

    override fun onAttachView(view: StartupView) {
        super.onAttachView(view)
    }

    override fun onTap() {
        if (!animating) {
            animating = true
            view?.onScreenTapped()
        }
    }

    override fun onAnimationFinished() {
        animating = false
        view?.navigateToHomeView()
    }
}