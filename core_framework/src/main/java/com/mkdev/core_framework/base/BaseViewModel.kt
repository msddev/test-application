package com.mkdev.core_framework.base

import com.mkdev.core_framework.base.CoroutineUtility.coroutineDispatcherProvider
import com.mkdev.core_framework.common.arch.CoreViewModel
import com.mkdev.core_framework.common.arch.StatefulViewModel
import com.mkdev.core_framework.common.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoroutineUtility {

    @JvmStatic
    fun coroutineDispatcherProvider() = object : CoroutineDispatcherProvider {
        override fun bgDispatcher(): CoroutineDispatcher {
            return Dispatchers.Default
        }

        override fun ioDispatcher(): CoroutineDispatcher {
            return Dispatchers.IO
        }

        override fun uiDispatcher(): CoroutineDispatcher {
            return Dispatchers.Main
        }

        override fun immediateDispatcher(): CoroutineDispatcher {
            return Dispatchers.Main.immediate
        }
    }
}

open class BaseViewModel : CoreViewModel(coroutineDispatcherProvider())
open class BaseStatefulViewModel<State : Any>(
    private val initialState: State,
    dispatcherProvider: CoroutineDispatcherProvider = coroutineDispatcherProvider()
) : StatefulViewModel<State>(initialState, dispatcherProvider, true)