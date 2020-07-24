package com.mkdev.core_framework.utils

import com.mkdev.core_framework.common.arch.StatefulViewModel

/**
 * There are some functionalities needed by fragments
 * So all base fragments should implement this
 */
interface FragmentInterface {
    fun <STATE : Any> StatefulViewModel<STATE>.subscribe(stateChange: (STATE) -> Unit)
    fun <STATE : Any> StatefulViewModel<STATE>.subscribeOnView(stateChange: (STATE) -> Unit)
}