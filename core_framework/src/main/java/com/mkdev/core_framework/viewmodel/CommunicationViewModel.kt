package com.mkdev.core_framework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mkdev.core_framework.utils.SingleLiveEvent

class CommunicationViewModel : ViewModel() {
    private val communicationChannel = SingleLiveEvent<Pair<Any, Any>?>()
    fun getEventsLiveData(): LiveData<Pair<Any, Any>?> {
        return communicationChannel
    }

    fun onResultProvided(request: Any, result: Any) {
        communicationChannel.value = Pair(request, result)
    }

    fun onResultConsumed(request: Any, result: Any) {
        if (communicationChannel.value == Pair(request, result)) {
            communicationChannel.value = null
        }
    }
}