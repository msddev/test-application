package com.mkdev.core_framework.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatcherProvider {
    fun bgDispatcher(): CoroutineDispatcher
    fun uiDispatcher(): CoroutineDispatcher
    fun ioDispatcher(): CoroutineDispatcher
    fun immediateDispatcher(): CoroutineDispatcher = uiDispatcher()
}