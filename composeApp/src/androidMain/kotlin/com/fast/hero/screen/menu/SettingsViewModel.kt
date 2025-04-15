package com.fast.hero.screen.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.fast.hero.FastHeroApplication
import com.fast.hero.sdk.FastCore
import com.fast.hero.ui.screens.settings.SettingOptionList
import com.fast.hero.sdk.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    app: Application
) : AndroidViewModel(app) {

    val fastCore = (app as FastHeroApplication).fastCore

    private val _fastingOptions = MutableStateFlow<List<SettingOptionList>>(emptyList())
    val fastingOptions = _fastingOptions.asStateFlow()
    
    fun sortRecommendationList(
        difficulty: Int,
        compromise: Int,
        doability: Int
    ) {
        _fastingOptions.value = fastCore.sortRecommendationList(difficulty, compromise, doability)
    }
    

}