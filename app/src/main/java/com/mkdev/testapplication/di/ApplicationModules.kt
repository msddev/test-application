package com.mkdev.testapplication.di

import com.mkdev.core_framework.base.CoroutineUtility.coroutineDispatcherProvider
import com.mkdev.home.di.featureHomeModule
import com.mkdev.local.di.localModule
import com.mkdev.remote.di.createRemoteModule
import com.mkdev.repository.di.repositoryModule
import org.koin.core.module.Module
import org.koin.dsl.module

private val mainModule = module {
    single {
        coroutineDispatcherProvider()
    }
}

val koinApplicationModules: List<Module> = listOf(
    mainModule,
    createRemoteModule("https://jsonplaceholder.typicode.com/"),
    repositoryModule,
    featureHomeModule,
    localModule
)