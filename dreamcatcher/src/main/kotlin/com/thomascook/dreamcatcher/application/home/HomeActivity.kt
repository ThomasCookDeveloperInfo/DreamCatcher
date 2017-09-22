package com.thomascook.dreamcatcher.application.home

import com.thomascook.dreamcatcher.application.AndroidInjectionProvider
import com.thomascook.dreamcatcher.presenter.HomePresenter
import com.thomascook.dreamcatcher.presenter.HomeView
import net.grandcentrix.thirtyinch.TiActivity

private const val FRAGMENT_TAG_DREAMS = "dreams"

/**
 * Main home activity that acts as a simple container to other views that are implemented mostly
 * as fragments
 */
class HomeActivity : TiActivity<HomePresenter, HomeView>(), HomeView {

    override fun providePresenter(): HomePresenter =
            AndroidHomePresenter(this, AndroidInjectionProvider())

    override fun showDreams() {
        // Display fragment that shows dreams
        if (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_DREAMS) != null) {
            return
        }

        // Inflate the dreams fragment
        val fragment = FragmentDreams.newInstance()
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(android.R.id.content, fragment, FRAGMENT_TAG_DREAMS)
                .setBreadCrumbTitle("Dreams")
                .commit()
    }
}