package com.fast.hero

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.fast.hero.ui.screens.main.MainScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FastHero",
    ) {
        MainScreen()
    }
}