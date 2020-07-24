package com.mkdev.core_framework.common.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mkdev.core_framework.common.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class CoreViewModel(
    protected val coroutineContexts: CoroutineDispatcherProvider
) : ViewModel(), CoroutineScope {

    protected val job = SupervisorJob()

    protected val scope = CoroutineScope(uiDispatcher() + job)

    protected fun bgDispatcher(): CoroutineDispatcher {
        return coroutineContexts.bgDispatcher()
    }

    protected fun uiDispatcher(): CoroutineDispatcher {
        return coroutineContexts.uiDispatcher()
    }

    protected fun immediateDispatcher(): CoroutineDispatcher {
        return coroutineContexts.immediateDispatcher()
    }

    protected fun ioDispatcher(): CoroutineDispatcher {
        return coroutineContexts.uiDispatcher()
    }

    suspend inline fun <T> onBg(crossinline coroutine: suspend () -> T): T {
        return withContext(bgDispatcher()) {
            coroutine()
        }
    }


    suspend inline fun <T> onIO(crossinline coroutine: suspend () -> T): T {
        return withContext(ioDispatcher()) {
            coroutine()
        }
    }

    suspend inline fun <T> onUI(crossinline coroutine: suspend () -> T): T {
        return withContext(uiDispatcher()) {
            coroutine()
        }
    }

    suspend inline fun <T> onUIImmediate(crossinline coroutine: suspend () -> T): T {
        return withContext(immediateDispatcher()) {
            coroutine()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = scope.coroutineContext

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }

    suspend fun <X> MutableLiveData<X>.update(newValue: X) {
        onUI {
            this.value = newValue
        }
    }

}