package com.mkdev.home.di

import com.mkdev.home.HomeViewModel
import com.mkdev.home.domain.GetPhotosUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureHomeModule = module {
    factory { GetPhotosUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}