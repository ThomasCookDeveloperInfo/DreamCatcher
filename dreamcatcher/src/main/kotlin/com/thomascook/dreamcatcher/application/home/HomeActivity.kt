package com.thomascook.dreamcatcher.application.home

import com.thomascook.dreamcatcher.application.AndroidInjectionProvider
import com.thomascook.dreamcatcher.presenter.HomePresenter
import com.thomascook.dreamcatcher.presenter.HomeView
import net.grandcentrix.thirtyinch.TiActivity

/**
 * Main home activity that acts as a simple container to other views that are implemented mostly
 * as fragments
 */
class HomeActivity : TiActivity<HomePresenter, HomeView>(), HomeView {

    override fun providePresenter(): HomePresenter =
            AndroidHomePresenter(this, AndroidInjectionProvider())
}