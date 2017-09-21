package com.thomascook.dreamcatcher.application.storage

import com.google.common.base.Optional
import com.thomascook.dreamcatcher.application.RepositoryException
import com.thomascook.dreamcatcher.application.storage.AndroidRealmSchedulers
import com.thomascook.dreamcatcher.application.storage.RealmHelper
import com.thomascook.dreamcatcher.Model
import com.thomascook.dreamcatcher.storage.DreamRepository
import io.reactivex.Observable
import io.realm.Realm
import java.util.concurrent.Callable

class RealmDreamRepository : DreamRepository {
    override fun deleteDream(id: Int): Observable<Boolean> =
        Observable.fromCallable {
            val realm = Realm.getDefaultInstance()

            val dbDream = realm.where(DBDream::class.java)
                    .equalTo("id", id)
                    .findFirst()

            if (dbDream != null) {
                RealmHelper.executeTransaction(realm, Callable<Unit> {
                    dbDream.deleteFromRealm()
                })
                realm.close()
                true
            } else {
                realm.close()
                false
            }
        }.subscribeOn(AndroidRealmSchedulers.realmThread())

    override fun addDream(dream: Model.Dream): Observable<Model.Dream> =
        Observable.fromCallable {

            // Set return value to input
            var returnDream: Model.Dream = dream

            val realm = Realm.getDefaultInstance()

            try {
                RealmHelper.executeTransaction(realm, Callable<Unit> {
                    var dbDream = realm.where(DBDream::class.java)
                            .equalTo("id", dream.id)
                            .findFirst()

                    if (dbDream != null) {
                        dbDream.time = dream.time
                        dbDream.name = dream.name
                        dbDream.content = dream.content
                    } else {
                        dbDream = DBDream(dream.id,
                                dream.time,
                                dream.name,
                                dream.content)
                    }

                    realm.insertOrUpdate(dbDream)

                    returnDream = Model.Dream(dbDream.id,
                            dbDream.time,
                            dbDream.name,
                            dbDream.content)
                })
            } catch (ex: RepositoryException) {
                throw ex
            }
            realm.close()
            returnDream
        }.subscribeOn(AndroidRealmSchedulers.realmThread())

    override fun getDream(id: Int): Observable<Optional<Model.Dream>> =
        Observable.fromCallable {
            Realm.getDefaultInstance().use { realm ->
                val dbDream = realm.where(DBDream::class.java)
                        .equalTo("id", id)
                        .findFirst()

                var dream: Model.Dream? = null
                if (dbDream != null) {
                    dream = Model.Dream(dbDream.id,
                            dbDream.time,
                            dbDream.name,
                            dbDream.content)
                }
                realm.close()
                Optional.fromNullable(dream)
            }
        }.subscribeOn(AndroidRealmSchedulers.realmThread())
}