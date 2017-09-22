package com.thomascook.dreamcatcher.application.splash

import android.content.Context
import com.thomascook.dreamcatcher.application.InjectorProvider
import com.thomascook.dreamcatcher.presenter.SplashPresenter
import com.thomascook.dreamcatcher.presenter.SplashView

private const val TAG = "AndroidStartupPresenter"

class AndroidSplashPresenter(private val context: Context,
                              injectorProvider: InjectorProvider) : SplashPresenter() {

    private var animating = false

    override fun onAttachView(view: SplashView) {
        super.onAttachView(view)
    }

    override fun onTap() {
        if (!animating) {
            animating = true
            view?.onScreenTapped()
        }
    }

    override fun onAnimationFinished() {
        view?.navigateToHomeView()
    }
}