package com.fast.hero.models

data class UserPreferences(
    val selectedFast: String = "",
    val fastStartTimeInMillis: Long? = null,
    val isRunning: Boolean = false,
    val isFirstTime: Boolean = true,
    val hasCompletedFast: Boolean = false
)

data class FastingMetrics(
    val id: String,
    val difficulty: Int,
    val compromise: Int,
    val doability: Int
)