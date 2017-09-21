package com.thomascook.dreamcatcher.application.storage

import android.support.test.runner.AndroidJUnit4
import com.thomascook.dreamcatcher.application.Environment
import com.thomascook.dreamcatcher.Model
import com.thomascook.dreamcatcher.application.storage.RealmDreamRepository
import io.realm.Realm
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * Set of instrumentation tests for verifying the integrity of the RealmDreamRepository implementation
 */
@RunWith(AndroidJUnit4::class)
class RealmDreamRepositoryInstrumentationTests {

    @After
    fun teardown() {
        val realm = Realm.getDefaultInstance()
        RealmHelper.executeTransaction(realm, Callable<Unit> {
            realm.deleteAll()
        })
    }

    // Tests that deleteDream successfully deletes a dream when the dream exists
    @Test
    @Throws(Exception::class)
    fun deleteDreamSuccessful() {
        // ARRANGE
        val dream = Model.Dream(0, Date(), "", "")
        val realmRepository = RealmDreamRepository()

        // ACT
        val testObservable = Environment.get().flatMap { _ ->
            realmRepository.addDream(dream).flatMap {
                realmRepository.deleteDream(0)
            }
        }.test()
        testObservable.awaitTerminalEvent(3, TimeUnit.SECONDS)

        // ASSERT
        testObservable.assertNoErrors()
        testObservable.assertValue { value -> value }
    }

    // Tests that deleteDream returns false when trying to delete
    // a dream that doesn't exist
    @Test
    @Throws(Exception::class)
    fun deleteDreamUnsuccessful() {
        // ARRANGE
        val realmRepository = RealmDreamRepository()

        // ACT
        val testObservable = Environment.get().flatMap { _ ->
            realmRepository.deleteDream(0)
        }.test()
        testObservable.awaitTerminalEvent(3, TimeUnit.SECONDS)

        // ASSERT
        testObservable.assertNoErrors()
        testObservable.assertValue { value -> !value }
    }

    // Tests that addDream successfully inserts a dream
    @Test
    @Throws(Exception::class)
    fun addNewDreamSuccessful() {
        // ARRANGE
        val dream = Model.Dream(0, Date(), "", "")
        val realmRepository = RealmDreamRepository()

        // ACT
        val testObservable = Environment.get().flatMap { _ ->
            realmRepository.addDream(dream)
        }.test()
        testObservable.awaitTerminalEvent(3, TimeUnit.SECONDS)

        // ASSERT
        testObservable.assertNoErrors()
        testObservable.assertValue { value -> value == dream }
    }

    // Tests that addDream successfully updates an existing dream
    @Test
    @Throws(Exception::class)
    fun addExistingDream() {
        // ARRANGE
        val dream = Model.Dream(0, Date(), "", "")
        val realmRepository = RealmDreamRepository()

        // ACT
        val testObservable = Environment.get().flatMap { _ ->
            realmRepository.addDream(dream).flatMap { _ ->
                realmRepository.addDream(dream)
            }
        }.test()
        testObservable.awaitTerminalEvent(3, TimeUnit.SECONDS)

        // ASSERT
        testObservable.assertNoErrors()
        testObservable.assertValue { value -> value == dream }
    }

    // Tests that getDream successfully returns a country from realm if the dream exists
    @Test
    @Throws(Exception::class)
    fun getExistingCountry() {
        // ARRANGE
        val dream = Model.Dream(0, Date(), "", "")
        val realmRepository = RealmDreamRepository()

        // ACT
        val testObservable = Environment.get().flatMap { _ ->
            realmRepository.addDream(dream).flatMap { _ ->
                realmRepository.getDream(0)
            }
        }.test()
        testObservable.awaitTerminalEvent(3, TimeUnit.SECONDS)

        // ASSERT
        testObservable.assertNoErrors()
        testObservable.assertValue { value -> value.isPresent }
        testObservable.assertValue { value -> value.get() == dream }
    }

    // Tests that getDream returns null from realm if dream does not exist
    @Test
    @Throws(Exception::class)
    fun getNonExistantDream() {
        // ARRANGE
        val realmRepository = RealmDreamRepository()

        // ACT
        val testObservable = Environment.get().flatMap { _ ->
            realmRepository.getDream(0)
        }.test()
        testObservable.awaitTerminalEvent(3, TimeUnit.SECONDS)

        // ASSERT
        testObservable.assertNoErrors()
        testObservable.assertValue { value -> !value.isPresent }
    }
}