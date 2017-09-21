package com.thomascook.dreamcatcher.application

import io.reactivex.Observable

/**
 * Object injector provider. Used to get references to
 */
interface InjectorProvider {
    fun provideEnvironment(): Observable<Environment>
}

class AndroidInjectionProvider : InjectorProvider {
    override fun provideEnvironment(): Observable<Environment> = Environment.get()
}