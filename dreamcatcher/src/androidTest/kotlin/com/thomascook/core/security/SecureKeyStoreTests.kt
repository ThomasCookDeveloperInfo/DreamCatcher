package com.thomascook.core.security

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val testKeyAlias = "testKey"

@RunWith(AndroidJUnit4::class)
class SecureKeyStoreTests {

    lateinit private var context: Context

    @Before
    fun setup() {
        // Use "TargetContext" and not "Context" otherwise preferences are lost between calls...
        context = InstrumentationRegistry.getTargetContext()
    }

    @After
    fun tearDown() {
        if (SecureKeyStore.hasKey(context, testKeyAlias)) {
            SecureKeyStore.deleteKey(context, testKeyAlias)
        }
    }

    @Test
    fun endToEndSecureKeyStoreTest() {
        // Assert that the key store does not have the key
        Assert.assertFalse(SecureKeyStore.hasKey(context, testKeyAlias))

        // Assert that key store can create key successfully
        Assert.assertNotNull(SecureKeyStore.createKey(context, testKeyAlias))

        // Assert that key store has the key
        Assert.assertTrue(SecureKeyStore.hasKey(context, testKeyAlias))

        // Get the key
        val key = SecureKeyStore.getKey(context, testKeyAlias)

        // Assert the key was created correctly
        Assert.assertNotNull(key)

        // Assert that the key was successfully deleted
        Assert.assertTrue(SecureKeyStore.deleteKey(context, testKeyAlias))
    }
}