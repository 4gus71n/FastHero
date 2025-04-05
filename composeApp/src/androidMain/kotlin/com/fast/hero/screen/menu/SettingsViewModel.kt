package com.fast.hero.screen.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fast.hero.ui.screens.settings.SettingOptionList
import com.fast.hero.ui.utils.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.abs

class SettingsViewModel : ViewModel() {

    private val _fastingOptions = MutableStateFlow<List<SettingOptionList>>(MockData.fastingOptions)
    val fastingOptions = _fastingOptions.asStateFlow()

    data class FastingMetrics(
        val id: String,
        val difficulty: Int,
        val compromise: Int,
        val doability: Int
    )

    val wizardMatrix = listOf(
        FastingMetrics(id = "12:12", difficulty = 2, compromise = 9, doability = 10),
        FastingMetrics(id = "14:10", difficulty = 4, compromise = 2, doability = 9),
        FastingMetrics(id = "16:8", difficulty = 5, compromise = 3, doability = 8),
        FastingMetrics(id = "18:6", difficulty = 7, compromise = 4, doability = 6),
        FastingMetrics(id = "20:4", difficulty = 8, compromise = 6, doability = 4),
        FastingMetrics(id = "OMAD", difficulty = 9, compromise = 7, doability = 3),
        FastingMetrics(id = "14:10 Crescendo", difficulty = 7, compromise = 1, doability = 9)
    )
    
    fun sortRecommendationList(
        difficulty: Int,
        compromise: Int,
        doability: Int
    ) {
        val fastingOptions = MockData.fastingOptions

        val matchingScores = fastingOptions.map { option ->
            val metrics = wizardMatrix.find { it.id == option.id }
            val score = metrics?.let {
                abs(
                    (it.difficulty - difficulty) +
                    (it.compromise - compromise) +
                    (it.doability - doability)
                )
            } ?: 0
            Pair(option.id, score)
        }

        val newSortedList = matchingScores
            .sortedBy { it.second }
            .mapNotNull { pair -> fastingOptions.find { f -> f.id == pair.first } }

        _fastingOptions.value = newSortedList
    }
    

}