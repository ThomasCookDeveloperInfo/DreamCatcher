package com.thomascook.dreamcatcher.application.startup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thomascook.dreamcatcher.application.AndroidInjectionProvider
import com.thomascook.dreamcatcher.application.home.HomeActivity
import com.thomascook.dreamcatcher.presenter.StartupPresenter
import com.thomascook.dreamcatcher.presenter.StartupView
import com.thomascook.dreamcatcher.R
import com.thomascook.dreamcatcher.databinding.SplashAnimatedBinding
import net.grandcentrix.thirtyinch.TiActivity
import pl.droidsonroids.gif.GifDrawable

class StartupActivity : TiActivity<StartupPresenter, StartupView>(), StartupView {

    private var sceneRoot: ViewGroup? = null

    override fun providePresenter(): StartupPresenter =
            AndroidStartupPresenter(applicationContext, AndroidInjectionProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.sceneRoot = findViewById(android.R.id.content)
        this.sceneRoot?.setOnClickListener { presenter.onTap() }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onScreenTapped() {
        val animation = GifDrawable(resources, R.drawable.logo_animation)
        animation.addAnimationListener {
            animation.stop()
            presenter.onAnimationFinished()
        }

        val splashBinding = SplashAnimatedBinding.inflate(LayoutInflater.from(this))
        splashBinding.animation.setImageDrawable(animation)

        sceneRoot?.addView(splashBinding.root)
    }

    override fun navigateToHomeView() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}