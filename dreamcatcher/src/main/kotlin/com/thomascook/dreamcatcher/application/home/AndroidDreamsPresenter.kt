package com.thomascook.dreamcatcher.application.home

import android.content.Context
import com.thomascook.dreamcatcher.EventReporter
import com.thomascook.dreamcatcher.Model
import com.thomascook.dreamcatcher.application.InjectorProvider
import com.thomascook.dreamcatcher.application.storage.RealmDreamRepository
import com.thomascook.dreamcatcher.presenter.DreamsPresenter
import com.thomascook.dreamcatcher.presenter.DreamsView
import java.util.*

private const val TAG = "AndroidDreamsPresenter"

class AndroidDreamsPresenter(private val context: Context,
                             val injectorProvider: InjectorProvider) : DreamsPresenter() {

    override fun attachView(view: DreamsView) {
        super.attachView(view)

        injectorProvider.provideEnvironment()
            .subscribe ({ _ ->

                val dream = Model.Dream(0, Date(), "A weird dream",
                        "Well, that was a really weird dream")

                view.showDreams(listOf(dream))

                val dreamRepository = RealmDreamRepository()

                dreamRepository.getDreams().subscribe ({ dreams ->
                    view.showDreams(listOf(dream))
                }, { error ->
                    EventReporter.e(TAG, "Failed to get dreams from realm", error)
                })
            }, { error ->
                EventReporter.e(TAG, "Failed to get environment", error)
            })
    }

    override fun onListViewShown() {

    }
}