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

                val dreamRepository = RealmDreamRepository()

                dreamRepository.getDreams().subscribe ({ dreams ->

                    // Dummy, replace with dreams from realm
                    val dream1 = Model.Dream(0, Date(), "A weird dream",
                            "Well, that was a really weird dream")

                    val dream2 = Model.Dream(1, Date(), "A weird dream",
                            "Well, that was a really weird dream")

                    val dream3 = Model.Dream(2, Date(), "A weird dream",
                            "Well, that was a really weird dream")

                    view.showDreams(listOf(dream1, dream2, dream3))
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