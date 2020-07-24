package com.mkdev.testapplication.di

import com.mkdev.home.di.featureHomeModule
import com.mkdev.local.di.localModule
import com.mkdev.remote.di.createRemoteModule
import com.mkdev.repository.di.repositoryModule
import org.koin.core.module.Module

val koinApplicationModules: List<Module> = listOf(
    createRemoteModule("https://jsonplaceholder.typicode.com/"),
    repositoryModule,
    featureHomeModule,
    localModule
)