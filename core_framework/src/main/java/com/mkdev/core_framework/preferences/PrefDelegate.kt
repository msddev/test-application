package com.mkdev.core_framework.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.mkdev.core_framework.extention.JsonString
import com.mkdev.core_framework.extention.fromJson
import com.mkdev.core_framework.extention.serializeByGson
import com.mkdev.core_framework.utils.EN
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Android Shared Preferences Delegate for Kotlin
 *
 * Usage:
 *
 * PrefDelegate.init(context)
 * ...
 * var accessToken by stringPref(PREFS_ID, "access_token")
 * var appLaunchCount by intPref(PREFS_ID, "app_launch_count", 0)
 * var autoRefreshEnabled by booleanPref("auto_refresh enabled") // using Default Shared Preferences
 *
 */
abstract class PrefDelegate<T>(private val prefName: String?, val prefKey: String) {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        /**
         * Initialize PrefDelegate with a Context reference
         * !! This method needs to be called before any other usage of PrefDelegate !!
         */
        fun init(context: Context) {
            Companion.context = context
        }
    }

    protected val prefs: SharedPreferences by lazy {
        context?.let {
            if (prefName != null) {
                it.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            } else {
                PreferenceManager.getDefaultSharedPreferences(it)
            }
        } ?: run {
            throw IllegalStateException(
                "Context was not initialized. Call PrefDelegate.init(context) before using it"
            )
        }
    }

    abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
    abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

fun stringPref(prefKey: String, defaultValue: String? = null) = StringPrefDelegate(
    null, prefKey,
    defaultValue
)

fun stringPref(prefName: String, prefKey: String, defaultValue: String? = null) =
    StringPrefDelegate(prefName, prefKey, defaultValue)

open class GsonPref<T>(
    private val gson: Gson,
    prefKey: String,
    private val defaultValue: T,
    private val tClass: Class<T>
) : PrefDelegate<T>(null, prefKey) {
    private var gsonInternal: String? by stringPref(prefKey, defaultValue?.toJson())
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return gsonInternal?.toObject() ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        gsonInternal = value.toJson()
    }

    private fun T.toJson(): String? {
        if (this == null)
            return null
        return gson.toJson(this)
    }

    private fun String.toObject(): T {
        return gson.fromJson<T>(this, tClass)
    }
}

inline fun <reified T> gsonPref(gson: Gson, prefKey: String, defaultValue: T) =
    GsonPref<T>(gson, prefKey, defaultValue, T::class.java)

open class StringPrefDelegate(prefName: String?, prefKey: String, val defaultValue: String?) :
    PrefDelegate<String?>(prefName, prefKey) {

    override fun getValue(thisRef: Any?, property: KProperty<*>) =
        prefs.getString(prefKey, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) =
        prefs.edit().putString(prefKey, value).apply()
}

fun intPref(prefKey: String, defaultValue: Int = 0) = IntPrefDelegate(null, prefKey, defaultValue)
fun intPref(prefName: String, prefKey: String, defaultValue: Int = 0) =
    IntPrefDelegate(prefName, prefKey, defaultValue)

class IntPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Int) :
    PrefDelegate<Int>(prefName, prefKey) {

    override fun getValue(thisRef: Any?, property: KProperty<*>) =
        prefs.getInt(prefKey, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) =
        prefs.edit().putInt(prefKey, value).apply()
}

fun floatPref(prefKey: String, defaultValue: Float = 0f) =
    FloatPrefDelegate(null, prefKey, defaultValue)

fun floatPref(prefName: String, prefKey: String, defaultValue: Float = 0f) =
    FloatPrefDelegate(prefName, prefKey, defaultValue)

class FloatPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Float) :
    PrefDelegate<Float>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) =
        prefs.getFloat(prefKey, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) =
        prefs.edit().putFloat(prefKey, value).apply()
}

fun booleanPref(prefKey: String, defaultValue: Boolean = false) =
    BooleanPrefDelegate(null, prefKey, defaultValue)

fun booleanPref(prefName: String, prefKey: String, defaultValue: Boolean = false) =
    BooleanPrefDelegate(prefName, prefKey, defaultValue)

class BooleanPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Boolean) :
    PrefDelegate<Boolean>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getBoolean(
        prefKey,
        defaultValue
    )

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) =
        prefs.edit().putBoolean(prefKey, value).apply()
}

fun longPref(prefKey: String, defaultValue: Long = 0L) =
    LongPrefDelegate(null, prefKey, defaultValue)

fun longPref(prefName: String, prefKey: String, defaultValue: Long = 0L) =
    LongPrefDelegate(prefName, prefKey, defaultValue)

class LongPrefDelegate(prefName: String?, prefKey: String, private val defaultValue: Long) :
    PrefDelegate<Long>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = prefs.getLong(
        prefKey,
        defaultValue
    )

    override fun setValue(
        thisRef: Any?, property: KProperty<*>,
        value: Long
    ) = prefs.edit().putLong(prefKey, value).apply()
}

fun stringSetPref(prefKey: String, defaultValue: Set<String> = HashSet<String>()) =
    StringSetPrefDelegate(null, prefKey, defaultValue)

fun stringSetPref(
    prefName: String,
    prefKey: String,
    defaultValue: Set<String> = HashSet<String>()
) = StringSetPrefDelegate(prefName, prefKey, defaultValue)

fun <T : Serializable> serializablePref(
    prefKey: String,
    prefName: String? = null,
    defaultValue: String = "",
    gson: Gson,
    clazz: Class<T>
) = SerializablePrefDelegate(prefName, prefKey, defaultValue, gson, clazz)

class StringSetPrefDelegate(
    prefName: String?, prefKey: String,
    private val defaultValue: Set<String>
) : PrefDelegate<Set<String>>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String> =
        prefs.getStringSet(prefKey, defaultValue) ?: emptySet()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>) =
        prefs.edit().putStringSet(prefKey, value).apply()
}

class LocalePref : StringPrefDelegate(
    prefKey = PrefKeys.LOCALE, prefName = null,
    defaultValue = EN
) {
    private var lastValue: String? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        if (lastValue == null) {
            lastValue = super.getValue(thisRef, property)
        }
        return lastValue!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        super.setValue(thisRef, property, value)
        lastValue = value
    }

    private object Holder {
        @SuppressLint("StaticFieldLeak")
        internal val INSTANCE = LocalePref()
    }

    companion object {
        val instance: LocalePref by lazy { Holder.INSTANCE }
    }
}

class SerializablePrefDelegate<T : Serializable>(
    prefName: String?, prefKey: String,
    private val defaultValue: String,
    private val gson: Gson,
    private val clazz: Class<T>
) : PrefDelegate<T?>(prefName, prefKey) {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? =
        JsonString((prefs.getString(prefKey, defaultValue) ?: "")).fromJson(gson, clazz)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) =
        prefs.edit().putString(prefKey, value?.serializeByGson(gson)).apply()
}