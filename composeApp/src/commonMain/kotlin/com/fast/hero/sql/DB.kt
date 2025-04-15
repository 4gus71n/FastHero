package com.fast.hero.sql

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import com.fast.hero.AppDatabase
import com.fast.hero.models.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.shareIn

class UserPreferencesRepository(sqlDriver: SqlDriver) {

    private val database = AppDatabase(sqlDriver)
    private val queries = database.userPreferencesQueries

    fun savePreferences(preferences: UserPreferences) {
        queries.insertOrReplace(
            selectedFast = preferences.selectedFast,
            fastStartTimeInMillis = preferences.fastStartTimeInMillis,
            isRunning = preferences.isRunning.toLong(),
            isFirstTime = preferences.isFirstTime.toLong(),
            hasCompletedFast = preferences.hasCompletedFast.toLong()
        )
    }

    fun observePreferences(): Flow<com.fast.hero.UserPreferences?> {
        return queries.selectPreferences()
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
    }

    fun getPreferences(): UserPreferences {
        return queries.selectPreferences().executeAsOneOrNull()?.let {
            UserPreferences(
                selectedFast = it.selectedFast,
                fastStartTimeInMillis = it.fastStartTimeInMillis,
                isRunning = it.isRunning != 0L,
                isFirstTime = it.isFirstTime != 0L
            )
        } ?: UserPreferences()
    }

    fun clearPreferences() {
        queries.deletePreferences()
    }

    fun updatePreferences(update: (UserPreferences) -> UserPreferences) {
        val current = getPreferences()
        savePreferences(update(current))
    }

    fun saveFastStartTimeInMillis(timeInMillis: Long?) =
        updatePreferences { it.copy(fastStartTimeInMillis = timeInMillis) }

    fun saveIsRunning(isRunning: Boolean) =
        updatePreferences { it.copy(isRunning = isRunning) }

    fun saveHasCompletedFast(hasCompletedFast: Boolean) =
        updatePreferences { it.copy(hasCompletedFast = hasCompletedFast) }

    fun saveIsFirstTime(isFirstTime: Boolean) =
        updatePreferences { it.copy(isFirstTime = isFirstTime) }

    fun saveSelectedFast(fastId: String) =
        updatePreferences { it.copy(selectedFast = fastId) }

    // --- Extension Helpers ---

    private fun Boolean.toLong() = if (this) 1L else 0L
}