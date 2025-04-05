package com.fast.hero

import androidx.compose.runtime.Composable
import com.fast.hero.ui.screens.main.MainScreen
import com.fast.hero.ui.screens.models.TimelineEntry
import com.github.guilhe.kmp.composeuiviewcontroller.ComposeUIViewController
import com.github.guilhe.kmp.composeuiviewcontroller.ComposeUIViewControllerState

data class MainViewState(
    val time: String,
    val progress: Float,
    val isRunning: Boolean,
    val feed: List<TimelineEntry>
)

@ComposeUIViewController
@Composable
fun MainViewScreen(
    @ComposeUIViewControllerState state: MainViewState,
    toggleTimer: () -> Unit,
) {
    MainScreen(
        onButtonClicked = toggleTimer,
        progress = state.progress,
        time = state.time,
        isRunning = state.isRunning,
        feed = state.feed
    )
}