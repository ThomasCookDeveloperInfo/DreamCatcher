package com.thomascook.dreamcatcher

import java.util.*

/**
 * Collection of application models
 */
class Model {
    data class Dream(val id: Int,
                     val time: Date,
                     val name: String,
                     val content: String)
}