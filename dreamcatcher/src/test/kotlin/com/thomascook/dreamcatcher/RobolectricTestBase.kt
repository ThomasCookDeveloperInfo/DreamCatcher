package com.thomascook.dreamcatcher

import android.app.Application
import android.content.Context
import com.thomascook.dreamcatcher.BuildConfig
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.File

/**
 * Base class for test that use robolectric
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(23),
        constants = BuildConfig::class,
        application = RobolectricTestBase.ApplicationStub::class,
        manifest = "AndroidManifest.xml")
abstract class RobolectricTestBase {

    fun cacheDir(): File = context().cacheDir

    fun context(): Context = RuntimeEnvironment.application

    internal class ApplicationStub : Application()
}