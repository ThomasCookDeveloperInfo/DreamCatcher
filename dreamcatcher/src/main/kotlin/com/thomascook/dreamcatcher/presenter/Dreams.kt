package com.thomascook.dreamcatcher.presenter

import com.thomascook.dreamcatcher.Model
import net.grandcentrix.thirtyinch.TiView

interface DreamsView : TiView {
    fun showDreams(dreams: Collection<Model.Dream>)
}

abstract class DreamsPresenter : PresenterBase<DreamsView>() {

    // Called when dream list view is show
    abstract fun onListViewShown()
}