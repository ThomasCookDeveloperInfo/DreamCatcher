package com.thomascook.dreamcatcher.application.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomascook.dreamcatcher.Model
import com.thomascook.dreamcatcher.application.AndroidInjectionProvider
import com.thomascook.dreamcatcher.databinding.FragmentDreamsBinding
import com.thomascook.dreamcatcher.presenter.DreamsPresenter
import com.thomascook.dreamcatcher.presenter.DreamsView
import net.grandcentrix.thirtyinch.TiFragment

private const val FRAGMENT_DREAMS_ALL = "FragmentDreamsAll"

class FragmentDreams : TiFragment<DreamsPresenter, DreamsView>(), DreamsView {
    private var viewBinding: FragmentDreamsBinding? = null

    private val handler = Handler()

    companion object {
        fun newInstance(): FragmentDreams {
            val fragment = FragmentDreams()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun providePresenter(): DreamsPresenter =
            AndroidDreamsPresenter(context, AndroidInjectionProvider())

    @SuppressLint("MissingSuperCall")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val _viewBinding = FragmentDreamsBinding.inflate(inflater ?: LayoutInflater.from(context))
        _viewBinding.toolbar.title ="Dreams"

        // Inflate the all dreams fragment
        val fragment = FragmentDreamsAll()
        childFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(fragment, FRAGMENT_DREAMS_ALL)
                .setBreadCrumbTitle("Dreams")
                .commit()

        viewBinding = _viewBinding

        return _viewBinding.root
    }

    override fun showDreams(dreams: Collection<Model.Dream>) {
        handler.post {
            val fragment = childFragmentManager.findFragmentByTag(FRAGMENT_DREAMS_ALL)
            when (fragment) {
                is FragmentDreamsAll -> {
                    fragment.showDreams(dreams)
                }
            }
        }
    }
}