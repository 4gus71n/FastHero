package com.fast.hero.ui.utils

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fast.hero.ui.screens.models.TimelineEntry
import com.fast.hero.ui.theme.FastHeroTheme

@Composable
@Preview
fun TimelineView_Preview() {
    FastHeroTheme {
        TimelineView(
            list = listOf(
                TimelineEntry(
                    title = "Title",
                    description = "Description",
                    emoji = "✅",
                    isPulsing = true
                ),
                TimelineEntry(
                    title = "Title",
                    description = "Description",
                    emoji = "✅",
                    isPulsing = true
                ),
                TimelineEntry(
                    title = "Title",
                    description = "Description",
                    emoji = "✅",
                    isPulsing = true
                )
            ),
            activeIndex = 1,
        )
    }
}

@Composable
fun TimelineItemView(
    item: TimelineEntry,
    isActive: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Box(modifier = Modifier.size(44.dp)) {
            if (item.isPulsing) {
                PulseEffect { TimelineEmoji(isActive, item) }
            } else {
                TimelineEmoji(isActive, item)
            }
        }

        Spacer(Modifier.width(18.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TimelineEmoji(isActive: Boolean, item: TimelineEntry) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                color = if (isActive) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(item.emoji, fontSize = 22.sp)
    }
}


@Composable
fun TimelineView(
    modifier: Modifier = Modifier,
    list: List<TimelineEntry>,
    activeIndex: Int // Track active milestone
) {
    Box(modifier = modifier.padding(vertical = 12.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            list.forEachIndexed { index, item ->
                TimelineItemView(item = item, isActive = index == activeIndex)
            }
        }
    }
}

