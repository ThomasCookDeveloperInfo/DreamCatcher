package com.thomascook.dreamcatcher.application.startup

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.thomascook.dreamcatcher.application.AndroidInjectionProvider
import com.thomascook.dreamcatcher.application.home.HomeActivity
import com.thomascook.dreamcatcher.presenter.StartupPresenter
import com.thomascook.dreamcatcher.presenter.StartupView
import com.thomascook.dreamcatcher.R
import com.thomascook.dreamcatcher.databinding.ViewLoginProgressBinding
import net.grandcentrix.thirtyinch.TiActivity

private const val TAG = "StartupActivity"
private const val ALPHA = "alpha"

class StartupActivity : TiActivity<StartupPresenter, StartupView>(), StartupView {

    private var sceneRoot: ViewGroup? = null

    override fun providePresenter(): StartupPresenter =
            AndroidStartupPresenter(applicationContext, AndroidInjectionProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Startup)
        this.sceneRoot = findViewById(android.R.id.content)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun showError(message: String) =
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun showProgress(show: Boolean, message: String) {
        if (show) {
            val viewBinding = ViewLoginProgressBinding.inflate(LayoutInflater.from(this))
            //Set message text
            viewBinding.message.text = if (message.isNotBlank()) message else null

            viewBinding.root.id = R.id.view_progress
            viewBinding.root.setBackgroundResource(android.R.color.white)

            sceneRoot?.addView(viewBinding.root)
            ObjectAnimator.ofFloat(viewBinding.root, ALPHA, 0.0f, 1.0f).start()

        } else {
            val progressView: View? = findViewById(R.id.view_progress) ?: return

            val valueAnimator = ObjectAnimator.ofFloat(progressView, ALPHA, 1.0f, 0.0f)
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    sceneRoot?.removeView(progressView)
                }
            })
            valueAnimator.start()
        }
    }

    override fun navigateToHomeView() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}