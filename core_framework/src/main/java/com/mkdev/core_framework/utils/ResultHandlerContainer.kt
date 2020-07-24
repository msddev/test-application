package com.mkdev.core_framework.utils

interface ResultHandlerContainer {
    fun onResultProvided(request: Any, result: Any): Boolean
}