package com.fast.hero.utils


import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import java.util.Date
import kotlin.reflect.KClass

interface SharedPreferencesProviderContract {

    /**
     * Checks whether the preferences contains a preference with the specified name.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     *         otherwise false.
     */
    fun contains(key: String): Boolean

    /**
     * Retrieves a value of type [T] from the preferences.
     *
     * @param T the type of the value to retrieve
     * @param key The name of the preference to retrieve.
     * @param default Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or [default]. Throws
     * [ClassCastException] if there is a preference with this name of a different type.
     */
    fun <T : Any> get(key: String, default: T): T

    /**
     * Retrieves a value of type [T] from the preferences or null if the preference does not exist.
     *
     * @param T the type of the value to retrieve.
     * @param key The name of the preference to retrieve.
     * @param type Type of the value to return.
     *
     * @return Returns the preference value if it exists, or **null**. Throws
     * [ClassCastException] if there is a preference with this name of a different type.
     */
    fun <T : Any> getOrNull(key: String, type: KClass<T>): T?

    /**
     * Returns a [Flow] observing values of type [T] from the preferences.
     *
     * @param T the type of the value to observe.
     * @param key The name of the preference to observe.
     * @param default Value to return if this preference does not exist.
     *
     * @return Returns a [Flow] of preference values starting with [default] if a value doesn't
     * exist. Throws [ClassCastException] if there is a preference with this name of a different
     * type.
     */
    fun <T : Any> observe(key: String, default: T): Flow<T>

    /**
     * Returns a [Flow] observing changes to the specified [keys]. When an observed value
     * changes, the corresponding key is emitted by the flow.
     *
     * @param keys The names of the preference keys to observe.
     *
     * @return Returns a [Flow] of preference keys when changes occur.
     */
    fun observe(keys: List<String>): Flow<String>

    /**
     * Set a value of type [T] in the preferences.
     *
     * @param T the type of the value to modify.
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @param immediate If true it will save the value synchronously. If false it will do it in the
     * background. It defaut value is false.
     */
    fun <T : Any> put(key: String, value: T?, immediate: Boolean = false)

    /**
     * Removes a value from preferences.
     *
     * @param key The name of the preference to remove.
     */
    fun remove(key: String)
}

class SharedPreferencesProvider(
    private val preferences: SharedPreferences
) : SharedPreferencesProviderContract {

    override fun contains(key: String): Boolean {
        return preferences.contains(key)
    }

    override fun <T : Any> get(key: String, default: T): T {
        @Suppress("UNCHECKED_CAST")
        return when (default) {
            is String -> preferences.getString(key, default) as T
            is Int -> preferences.getInt(key, default) as T
            is Long -> preferences.getLong(key, default) as T
            is Float -> preferences.getFloat(key, default) as T
            is Boolean -> preferences.getBoolean(key, default) as T
            is Date -> Date(preferences.getLong(key, default.time)) as T
            is Set<*> -> preferences.getStringSet(key, default as Set<String>) as T
            else -> throw RuntimeException("Unsupported type!")
        }
    }

    override fun <T : Any> getOrNull(key: String, type: KClass<T>): T? {
        return if (preferences.contains(key)) {
            @Suppress("UNCHECKED_CAST")
            when (type) {
                // default values are dummy values since we've already checked that the key exists
                String::class -> preferences.getString(key, null) as T?
                Int::class -> preferences.getInt(key, 0) as T?
                Long::class -> preferences.getLong(key, 0L) as T?
                Float::class -> preferences.getFloat(key, 0.0f) as T?
                Boolean::class -> preferences.getBoolean(key, false) as T?
                Date::class -> Date(preferences.getLong(key, 0L)) as T?
                else -> {
                    if (Set::class.java.isAssignableFrom(type.java)) {
                        preferences.getStringSet(key, null) as T?
                    } else {
                        throw RuntimeException("Unsupported type!")
                    }
                }
            }
        } else {
            null
        }
    }

    override fun <T : Any> observe(key: String, default: T): Flow<T> {
        return callbackFlow {
            // listener for preference value changes
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, keyName ->
                if (key == keyName) {
                    trySend(get(key, default))
                }
            }

            // register listener and emit current/initial value
            preferences.registerOnSharedPreferenceChangeListener(listener)
            trySend(get(key, default))

            // wait for flow to complete and unregister listener
            awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
        }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override fun observe(keys: List<String>): Flow<String> {
        return callbackFlow {
            // listen for preference value changes and emit key name of changed value
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                key?.let {
                    if (key in keys) {
                        trySend(key)
                    }
                }
            }

            // register/unregister listener
            preferences.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
        }
            .flowOn(Dispatchers.IO)
    }

    override fun <T : Any> put(key: String, value: T?, immediate: Boolean) {
        preferences.edit().apply {
            @Suppress("UNCHECKED_CAST")
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is Date -> putLong(key, value.time)
                is Set<*> -> putStringSet(key, value as Set<String>)
                null -> putString(key, null)
                else -> throw RuntimeException("Unsupported type!")
            }

            @SuppressLint("ApplySharedPref")
            if (immediate) commit() else apply()
        }
    }

    override fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }

    fun deleteAll() {
        preferences.edit().clear().apply()
    }
}