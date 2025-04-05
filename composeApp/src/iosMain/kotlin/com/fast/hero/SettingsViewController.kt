package com.fast.hero

import androidx.compose.runtime.Composable
import com.fast.hero.ui.screens.main.MainScreen
import com.fast.hero.ui.screens.settings.SettingOptionList
import com.fast.hero.ui.screens.settings.SettingsScreen
import com.github.guilhe.kmp.composeuiviewcontroller.ComposeUIViewController
import com.github.guilhe.kmp.composeuiviewcontroller.ComposeUIViewControllerState

data class SettingsViewState(
    val optionList: List<SettingOptionList>
)

@ComposeUIViewController
@Composable
fun SettingsViewScreen(
    @ComposeUIViewControllerState state: SettingsViewState,
    onSlideChanged: ((Int, Int, Int) -> Unit),
    onOptionSelected: ((String) -> Unit),
    onBackButtonClicked: (() -> Unit)
) {
    SettingsScreen(
        optionList = state.optionList,
        onSlideChanged = onSlideChanged,
        onOptionSelected = onOptionSelected,
        onBackButtonClicked = onBackButtonClicked
    )
}