package com.thomascook.dreamcatcher.presenter

import net.grandcentrix.thirtyinch.TiView

/**
 * Contains view and presenter interfaces for the home activity
 */
interface HomeView : TiView {

    // Called to show dreams
    fun showDreams()
}

abstract class HomePresenter : PresenterBase<HomeView>() {

}