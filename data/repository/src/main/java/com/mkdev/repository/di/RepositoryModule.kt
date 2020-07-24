package com.mkdev.repository.di

import com.mkdev.repository.SampleRepository
import com.mkdev.repository.SampleRepositoryImpl
import com.mkdev.repository.AppDispatchers
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    factory { SampleRepositoryImpl(get(), get()) as SampleRepository }
}