package com.mkdev.core_framework.common.arch

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mkdev.core_framework.common.coroutines.CoroutineDispatcherProvider
import com.mkdev.core_framework.common.models.ViewStateStore
import kotlinx.coroutines.runBlocking

abstract class StatefulViewModel<STATE : Any>(
    initialState: STATE,
    coroutineContexts: CoroutineDispatcherProvider,
    emitInitialState: Boolean = false
) : CoreViewModel(coroutineContexts) {

    private val stateStore = ViewStateStore(initialState, emitInitialState)
    private val lazyState: ViewStateStore<STATE> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        stateStore.also {
            create()
        }
    }

    private fun create() {
        onCreate()

    }

    /**
     * Will be called only once the ViewModel gets subscribed.
     */
    protected open fun onCreate() {

    }

    fun stateLiveData(): LiveData<STATE> = lazyState.liveData

    fun observe(owner: LifecycleOwner, observer: (STATE) -> Unit) =
        lazyState.observe(owner, observer)

    fun observeForever(observer: Observer<STATE>) = lazyState.observeForever(observer)
    fun removeObserver(observer: Observer<STATE>) = lazyState.removeObserver(observer)
    val currentState: STATE
        get() {
            return stateStore.state
        }


    /**
     * Is called after the state is successfully updated
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    open fun onStateUpdated(currentState: STATE) {

    }

    /**
     * Used to update states outside of coroutines.
     * This method should only be called on the main thread since it is updating a LiveData.
     * If you are inside a coroutine and a context other than the main dispatcher, use onUI { applyState (...) }
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun applyState(function: STATE.() -> STATE) = runBlocking {
        return@runBlocking onUIImmediate {
            val oldState = stateStore.state
            val newState = function(oldState)
            if (newState == oldState)
                return@onUIImmediate // don't change the state, since we want distinct changes in the state

            stateStore.state = newState
            onStateUpdated(currentState)
        }
    }
}