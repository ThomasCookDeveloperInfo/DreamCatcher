package com.thomascook.dreamcatcher.presenter

import net.grandcentrix.thirtyinch.TiView

/**
 * Interface classes used to implement start up view/presenter
 */
interface StartupView : TiView {

    //Show an error message
    fun showError(message: String)

    //Call to show a progress view.
    fun showProgress(show: Boolean, message: String)

    //Call to instruct the view to show the main one.
    fun navigateToHomeView()
}

abstract class StartupPresenter : PresenterBase<StartupView>() {

}