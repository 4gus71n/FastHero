package com.fast.hero.sdk

import com.fast.hero.ui.screens.models.TimelineEntry
import com.fast.hero.sdk.MockData

class FeedGenerator {
    fun computeDefaultFeed(): List<TimelineEntry> {
        val tipList = MockData.defaultFeed
        return listOf(
            listOf(
                TimelineEntry(
                    title = "Did you know?",
                    description = "Fasting has many benefits for your health 👇",
                    emoji = "❓",
                    isPulsing = true
                )
            ),
            tipList
        ).flatten()
    }

    fun computeCongratsFeed(): List<TimelineEntry> {
        return listOf(
            TimelineEntry(
                title = "Hurray!",
                description = "You've successfully completed your fast! Great job!!",
                emoji = "🎉",
                isPulsing = true
            )
        )
    }

    fun computeHelpFeed(): List<TimelineEntry> {
        return listOf(
            TimelineEntry(
                title = "Let's go!",
                description = "Hit the start button to begin!",
                emoji = "🚀",
                isPulsing = true
            )
        )
    }

    /**
     * Formats the fasting time as hours and minutes.
     */
    private fun formatFeedTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "0m"
        }
    }

    fun computeWelcomeFeed(): List<TimelineEntry> {
        return listOf(
            TimelineEntry(
                title = "Welcome!",
                description = "Ready to begin your fasting journey? Tap the start button, and we'll help you find the fasting technique that suits you best!",
                emoji = "😼",
                isPulsing = true
            )
        )
    }

    fun computeFeed(currentTimeMillis: Long, fastStartDateInMillis: Long?): List<TimelineEntry> {
        val fastingStartedMillis = fastStartDateInMillis ?: return emptyList()

        return MockData.fastingMilestones.mapIndexed { index, milestone ->
            val thisMilestoneMillis = fastingStartedMillis + (milestone.timestampInSeconds.toLong() * 1000)
            val nextMilestone = MockData.fastingMilestones.getOrNull(index + 1)
            val nextMilestoneMillis = nextMilestone?.let {
                fastingStartedMillis.toLong() + (it.timestampInSeconds.toLong() * 1000L)
            }

            val completionState = when {
                currentTimeMillis >= thisMilestoneMillis -> if (nextMilestoneMillis == null || currentTimeMillis < nextMilestoneMillis) 0 else -1
                else -> 1
            }

            TimelineEntry(
                title = "${formatFeedTime(milestone.timestampInSeconds.toInt())} – ${milestone.title}",
                description = listOfNotNull(
                    milestone.description,
                    milestone.warnings?.let { "⚠️ $it" },
                    milestone.advice?.let { "ℹ️️ $it" }
                ).joinToString("\n"),
                emoji = when (completionState) {
                    0 -> "🔥"
                    -1 -> "✅"
                    else -> "☑️"
                },
                isPulsing = completionState == 0,
            )
        }
    }
}
