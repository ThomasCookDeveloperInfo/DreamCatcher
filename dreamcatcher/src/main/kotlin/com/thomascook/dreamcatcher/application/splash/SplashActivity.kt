package com.thomascook.dreamcatcher.application.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thomascook.dreamcatcher.application.AndroidInjectionProvider
import com.thomascook.dreamcatcher.application.home.HomeActivity
import com.thomascook.dreamcatcher.R
import com.thomascook.dreamcatcher.databinding.SplashAnimatedBinding
import com.thomascook.dreamcatcher.presenter.SplashPresenter
import com.thomascook.dreamcatcher.presenter.SplashView
import net.grandcentrix.thirtyinch.TiActivity
import pl.droidsonroids.gif.GifDrawable

class SplashActivity : TiActivity<SplashPresenter, SplashView>(), SplashView {

    private var sceneRoot: ViewGroup? = null
    private var currentViewBinding: android.databinding.ViewDataBinding? = null

    override fun providePresenter(): SplashPresenter =
            AndroidSplashPresenter(applicationContext, AndroidInjectionProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.sceneRoot = findViewById(android.R.id.content)

        val animation = GifDrawable(resources, R.drawable.logo_animation)
        animation.stop()

        val splashBinding = SplashAnimatedBinding.inflate(LayoutInflater.from(this))
        splashBinding.animation.setImageDrawable(animation)

        sceneRoot?.addView(splashBinding.root)

        currentViewBinding = splashBinding

        this.sceneRoot?.setOnClickListener { presenter.onTap() }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onScreenTapped() {
        if (currentViewBinding is SplashAnimatedBinding) {
            val splashBinding = (currentViewBinding as SplashAnimatedBinding)
            val animation = (splashBinding.animation.drawable as GifDrawable)
            animation.addAnimationListener {
                animation.stop()
                presenter.onAnimationFinished()
            }
            animation.seekToFrame(0)
            animation.start()
        }
    }

    override fun navigateToHomeView() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}