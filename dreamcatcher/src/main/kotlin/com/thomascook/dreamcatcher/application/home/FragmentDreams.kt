package com.thomascook.dreamcatcher.application.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomascook.core.utils.FragmentViewPagerAdapter
import com.thomascook.dreamcatcher.Model
import com.thomascook.dreamcatcher.R
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

        // Setup adapter and add FragmentDreamsAll
        val adapter = FragmentViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FragmentDreamsAll::class.java, null, getString(R.string.all_dreams))

        // Set view pager adapter
        _viewBinding.viewpager.adapter = adapter

        // Setup tabs with view pager
        _viewBinding.tabs.setupWithViewPager(_viewBinding.viewpager)

        _viewBinding.viewpager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
            internal var selectedPaage = -1

            override fun onPageSelected(position: Int) {
                selectedPaage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state != ViewPager.SCROLL_STATE_IDLE && selectedPaage != -1)
                    return

                when (selectedPaage) {
                    0 -> handler.post { presenter.onListViewShown() }
                }
            }
        })

        viewBinding = _viewBinding

        return _viewBinding.root
    }

    override fun showDreams(dreams: Collection<Model.Dream>) {
        handler.post {
            val fragment = (viewBinding?.viewpager?.adapter
                    as FragmentViewPagerAdapter?)?.currentPrimaryItem

            when (fragment) {
                is FragmentDreamsAll -> {
                    fragment.showDreams(dreams)
                }
            }
        }
    }
}