package com.thomascook.dreamcatcher.application

import android.content.Context
import android.preference.PreferenceManager
import com.thomascook.core.security.EncryptDecrypt
import com.thomascook.core.security.SecureKeyStore
import com.thomascook.core.utils.ApplicationPreferences
import java.io.Serializable

//Constants used for storing data
private const val KEY_APP_VERSION = "app_version"
private const val KEY_API_VERSION = "api_version"
private const val AES_KEY_ALIAS = "key_com.thomascook.dreamcatcher_encryption"

/**
 * Function for getting initialisation vector and AES key for encrypting/decrypting preferences.
 */
private fun getEncryptionKey(context: Context): ByteArray {
    return if (!SecureKeyStore.hasKey(context, AES_KEY_ALIAS)) {
        SecureKeyStore.createKey(context, AES_KEY_ALIAS)
    } else {
        SecureKeyStore.getKey(context, AES_KEY_ALIAS)
    }
}

/**
 * Used to access application specific preferences.
 * @property appVersion Value returned as part of server system info call.
 *                      Will be "" if the server doesn't support the call.
 * @property apiVersion Value returned as part of server system info call to specify server
 *                      supported api version. Will be "" if the server doesn't support the call.
 */
class Preferences constructor(val context: Context) {

    private val applicationPreferences = ApplicationPreferences(PreferenceManager.getDefaultSharedPreferences(context))

    fun apply(): Preferences {
        applicationPreferences.apply()
        return this
    }

    fun clearAll(): Preferences {
        applicationPreferences.clear()
        return this
    }

    private fun setPreference(key: String, value: String): Preferences {
        if (value.isBlank())
            applicationPreferences.remove(key)
        else
            applicationPreferences.setString(key, value)
        return this
    }

    /**
     * Function for storing serializable objects.
     * @param key Preference key used for storing
     * @param value Object that supports [Serializable] interface
     */
    private fun setPreference(key: String, value: Serializable?): Preferences {
        if (value == null)
            applicationPreferences.remove(key)
        else
            applicationPreferences.setSerializable(key, value)
        return this
    }

    /**
     * Function for storing serializable objects in encrypted form.
     * @param key Preference key used for storing
     * @param value Object that supports [Serializable] interface
     */
    private fun setPreferenceEnc(key: String, value: Serializable?): Preferences {
        if (value == null)
            applicationPreferences.remove(key)
        else {
            applicationPreferences.setSerializable(key, value, { toEncrypt ->
                EncryptDecrypt(getEncryptionKey(context)).encrypt(toEncrypt)
            })
        }
        return this
    }

    /**
     * Function for retrieving encrypted [Serializable] objects
     * @param key Preference key.
     */
    private fun <T : Serializable> getPreferenceEnc(key: String): T? {
        return applicationPreferences.getSerializable(key, { toDecrypt ->
            EncryptDecrypt(getEncryptionKey(context)).decrypt(toDecrypt)
        })
    }

    /**
     *  Property for getting/setting app version
     */
    var appVersion: String
        get() = applicationPreferences.getString(KEY_APP_VERSION)
        set(value) {
            setPreference(KEY_APP_VERSION, value)
        }

    /**
     * Property for getting/setting api version
     */
    var apiVersion: String
        get() = applicationPreferences.getString(KEY_API_VERSION)
        set(value) {
            setPreference(KEY_API_VERSION, value)
        }
}