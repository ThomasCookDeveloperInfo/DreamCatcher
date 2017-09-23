package com.thomascook.core.utils

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.ViewGroup

private val TAG = "FragmentPageAdapter"

class FragmentViewPagerAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager) {

    private val fragmentClasses = ArrayList<Class<out Fragment>>(10)
    private val fragmentParameters = ArrayList<Bundle?>(10)
    private val fragmentTitles = ArrayList<CharSequence?>(10)

    var currentPrimaryItem: Fragment? = null

    fun addFragment(fragmentClass: Class<out Fragment>, args: Bundle?, pageTitle: CharSequence?) {
        this.fragmentClasses.add(fragmentClass)
        this.fragmentParameters.add(args)
        this.fragmentTitles.add(pageTitle)
    }

    override fun getItem(position: Int): Fragment? {
        return try {
            val fragment = this.fragmentClasses[position].newInstance()
            fragment.arguments = this.fragmentParameters[position]
            fragment
        } catch (ex: Exception) {
            Log.d(TAG, "Failed to create fragment", ex)
            null
        }
    }

    override fun getCount(): Int = fragmentClasses.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentTitles[position]

    override fun setPrimaryItem(container: ViewGroup?, position: Int, item: Any?) {
        super.setPrimaryItem(container, position, item)
        currentPrimaryItem = item as Fragment?
    }
}