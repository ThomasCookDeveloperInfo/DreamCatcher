package com.thomascook.dreamcatcher.storage

import com.google.common.base.Optional
import com.thomascook.dreamcatcher.Model
import io.reactivex.Observable

interface DreamRepository {
    /**
     * CRUD methods for dreams
     */
    fun deleteDream(id: Int) : Observable<Boolean>
    fun addDream(dream: Model.Dream) : Observable<Model.Dream>
    fun getDream(id: Int) : Observable<Optional<Model.Dream>>
}