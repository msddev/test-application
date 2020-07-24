package com.mkdev.home

import com.mkdev.core_framework.base.BaseStatefulViewModel
import com.mkdev.core_framework.common.coroutines.CoroutineDispatcherProvider
import com.mkdev.home.domain.GetPhotosUseCase

class HomeViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider
) : BaseStatefulViewModel<HomeViewModel.ViewState>(
    ViewState,
    coroutineDispatcherProvider
) {
    object ViewState
}