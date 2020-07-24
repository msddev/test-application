package com.mkdev.core_framework.extention

import com.google.gson.Gson
import java.io.Serializable

inline fun <T : Serializable> T.serializeByGson(gson: Gson): String = gson.toJson(this)

fun <T : Serializable> JsonString.fromJson(gson: Gson, clazz: Class<T>): T? =
    kotlin.runCatching { gson.fromJson(this.json, clazz) }.getOrNull()

inline class JsonString(val json: String)