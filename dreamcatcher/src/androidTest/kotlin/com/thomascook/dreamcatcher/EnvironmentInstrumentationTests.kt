package com.thomascook.dreamcatcher

import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.thomascook.core.utils.ApplicationPreferences
import com.thomascook.dreamcatcher.application.Environment
import com.thomascook.dreamcatcher.application.Preferences
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

/**
 * Instrumentation splash_animated for testing app environment
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class EnvironmentInstrumentationTests {

    /**
     * Test that environment initialises without errors
     */
    @Test
    @Throws
    fun environmentInitialised() {
        //ARRANGE
        // Context of the app under splash_animated.
        val appContext = InstrumentationRegistry.getTargetContext()
        Environment.initialise(appContext)

        //ACT
        val testSubscription = Environment.get().test()
        testSubscription.awaitTerminalEvent(3, TimeUnit.SECONDS)

        //ASSERT
        testSubscription.assertNoErrors()
    }

    /**
     * The function tests that application preferences successfully stores and reads back encrypted
     * data
     */
    @Test
    @Throws
    fun preferencesEncryptionSuccess() {
        val appContext = InstrumentationRegistry.getTargetContext()

        //ARRANGE
        //Create preferences that give direct access to application managed preferences
        val preferences = ApplicationPreferences(PreferenceManager.getDefaultSharedPreferences(appContext))
        //Create application preferences
        val appPrefs = Preferences(appContext)
        //Clear all preferences
        appPrefs.clearAll().apply()

        //ACT
        //Save forms credentials (in encrypted form)
        val apiVersionToStore = "18"
        appPrefs.apply {
            apiVersion = apiVersionToStore
        }.apply()

        //ASSERT
        //Check that preferences exist and cannot be read directly
        Assert.assertTrue("Preference value has not been saved.", preferences.contains("api_version"))
        Assert.assertNull("Preference value could be read", preferences.getSerializable("api_version"))
        //Check that we can read back the value
        Assert.assertEquals("Stored api version doesn't match", apiVersionToStore, appPrefs.apiVersion)
    }
}
