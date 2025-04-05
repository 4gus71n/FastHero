package com.fast.hero.ui.screens.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fast.hero.ui.screens.models.TimelineEntry
import com.fast.hero.ui.utils.Timer
import com.fast.hero.ui.theme.FastHeroTheme
import com.fast.hero.ui.utils.TimelineView

@Composable
@Preview
fun FastingView() {
    MainScreen(
        progress = 100f
    )
}

@Composable
fun MainScreen(
    onButtonClicked: (() -> Unit) = {},
    progress: Float = 0f,
    time: String = "--:--",
    isRunning: Boolean = false,
    feed: List<TimelineEntry> = emptyList()
) {
    FastHeroTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))
                
                Timer(
                    modifier = Modifier.fillMaxSize(),
                    progress = progress,
                    elapsed = time,
                    isCountingDown = isRunning,
                    isRestarting = false,
                    isSetting = false,
                )

                Button(
                    modifier = Modifier
                        .padding(top = 70.dp)
                        .wrapContentSize(),
                    onClick = onButtonClicked,
                ) {
                    Text(
                        text = if (isRunning) "STOP FASTING" else "START",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                TimelineView(
                    modifier = Modifier,
                    list = feed,
                    activeIndex = feed.indexOfFirst { it.isPulsing }
                )
            }
        }
    }
}