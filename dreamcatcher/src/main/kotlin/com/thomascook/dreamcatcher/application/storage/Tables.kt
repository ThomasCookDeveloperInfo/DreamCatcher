package com.thomascook.dreamcatcher.application.storage

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class DBDream(@PrimaryKey
                   open var id: Int = 0,
                   open var time: Date = Date(),
                   open var name: String = "",
                   open var content: String = "") : RealmObject()