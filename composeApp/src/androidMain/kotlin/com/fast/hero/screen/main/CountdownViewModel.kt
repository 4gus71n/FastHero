package com.fast.hero.screen.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.fast.hero.FastHeroApplication
import kotlinx.coroutines.flow.*

class CountdownViewModel(
    app: Application
) : AndroidViewModel(app) {
    
    val fastCore = (app as FastHeroApplication).fastCore

    val time = fastCore.time
    val feed = fastCore.feed
    val progress = fastCore.progress
    
    val isFastingRunning = fastCore.observeIsFastRunning
    
    val shouldResumeFast = combine(
        fastCore.observeSelectedFastId,
        fastCore.observeIsFastRunning
    ) { fastId, isRunning ->
        "$fastId$isRunning"
    }
    .distinctUntilChanged()
    
    fun findSelectedFast() = fastCore.findSelectedFast()
    fun resumeFast() = fastCore.resumeFast()
    fun toggleTimer() = fastCore.toggleTimer()
    fun setSelectedFast(fastId: String) {
        fastCore.fastId = fastId
    }
    
}
