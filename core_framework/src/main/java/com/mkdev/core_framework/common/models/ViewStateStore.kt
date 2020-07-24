package com.mkdev.core_framework.common.models

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class ViewStateStore<T : Any>(
    initialState: T,
    emitInitialState: Boolean = false
) {
    var state = initialState
        set(value) {
            field = value
            internalLiveData.value = value
        }
    private val internalLiveData = MutableLiveData<T>().apply {
        if (emitInitialState)
            value = initialState
    }

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        internalLiveData.observe(owner, Observer { observer(it!!) })

    fun observeForever(observer: Observer<T>) =
        internalLiveData.observeForever(observer)

    fun removeObserver(observer: Observer<T>) = internalLiveData.removeObserver(observer)

    val liveData = internalLiveData as LiveData<T>
}