package com.thomascook.dreamcatcher.presenter

import net.grandcentrix.thirtyinch.TiView

/**
 * Interface classes used to implement start up view/presenter
 */
interface SplashView : TiView {

    // Call to show splash animation when user taps screne
    fun onScreenTapped()

    //Call to instruct the view to show the main one.
    fun navigateToHomeView()
}

abstract class SplashPresenter : PresenterBase<SplashView>() {

    // Called when user touches screen
    abstract fun onTap()

    // Called when splash animation has finished
    abstract fun onAnimationFinished()
}