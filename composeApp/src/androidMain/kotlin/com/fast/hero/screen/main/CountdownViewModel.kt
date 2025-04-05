package com.fast.hero.screen.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fast.hero.ui.screens.models.TimelineEntry
import com.fast.hero.ui.screens.settings.SettingOptionList
import com.fast.hero.ui.utils.MockData
import com.fast.hero.utils.SharedPreferencesProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.Date
import java.util.concurrent.TimeUnit

class CountdownViewModel(
    app: Application
) : AndroidViewModel(app) {

    // SharedPreferences provider for storing fasting-related data
    private val preferences = SharedPreferencesProvider(
        preferences = app.getSharedPreferences("shared_prefs3", Context.MODE_PRIVATE)
    )

    // State flow for the feed (timeline of fasting milestones)
    private val _feed = MutableStateFlow<List<TimelineEntry>>(emptyList())
    val feed: StateFlow<List<TimelineEntry>> = _feed.asStateFlow()

    // Gets or sets the fasting start date in shared preferences
    private var fastStartDate: Date?
        get() = preferences.get<Long>("started_at", 0).takeIf { it > 0 }?.let { Date(it) }
        set(value) {
            preferences.put("started_at", value?.time ?: 0)
        }

    // Gets or sets the fasting ID in shared preferences
    private var fastId: String
        get() = preferences.get("fast_id", "")
        set(value) {
            preferences.put("fast_id", value)
        }

    // Gets or sets the fasting timer state (running or stopped)
    private var isRunning: Boolean
        get() = preferences.get("is_running", false)
        set(value) {
            preferences.put("is_running", value)
        }

    private var hasCompletedFast: Boolean
        get() = preferences.get("has_completed_fast", false)
        set(value) {
            preferences.put("has_completed_fast", value)
        }

    // Tells if the app has been installed for the first time
    private var isFirstTime: Boolean
        get() = preferences.get("first_time", true)
        set(value) {
            preferences.put("first_time", value)
        }

    // Observes if the fasting is currently running
    val isFastingRunning: Flow<Boolean> = preferences.observe("is_running", false)

    private var countDownJob: Job? = null

    // State for the formatted time remaining (HH:MM:SS)
    private val _time = MutableStateFlow("--:--")
    val time: StateFlow<String> = _time.asStateFlow()

    // State for fasting progress (percentage)
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()
    
    val selectedFast = preferences.observe("fast_id", "").map { 
        findSelectedFast()
    }

    // Finds the selected fasting option based on stored fasting ID
    fun findSelectedFast(): SettingOptionList? =
        fastId.takeIf { it.isNotBlank() }?.let { id ->
            MockData.fastingOptions.find { it.id == id }
        }

    fun setSelectedFast(fastId: String) {
        this.fastId = fastId
    }

    /**
     * Computes the remaining fasting time in seconds.
     * Returns 0 if fasting is not active.
     */
    fun computeTimeLeft(): Int {
        val fast = findSelectedFast() ?: return 0
        val startDate = fastStartDate ?: return 0

        val expectedEnd = startDate.time + TimeUnit.SECONDS.toMillis(fast.fastingTimeInSeconds.toLong())
        val remainingMillis = expectedEnd - System.currentTimeMillis()

        return remainingMillis.coerceAtLeast(0).let {
            TimeUnit.MILLISECONDS.toSeconds(it).toInt()
        }
    }
    
    private fun computeWelcomeFeed() {
        _feed.value = listOf(
            TimelineEntry(
                title = "Welcome!",
                description = "Ready to begin your fasting journey? Tap the start button, and we'll help you find the fasting technique that suits you best!",
                emoji = "😼",
                isPulsing = true
            )
        )
    }

    private fun computeDefaultFeed() {
        val tipList = listOf(
            TimelineEntry(
                title = "Fat for Fuel!",
                description = "When you fast, your body switches from burning sugar to burning fat for energy. It's like flipping a metabolic switch! 🔥",
                emoji = "💪",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Autophagy Mode",
                description = "Fasting triggers autophagy, a process where your cells clean out damaged parts and recycle them. It's like a deep clean for your body! 🧹",
                emoji = "🧼",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Brain Booster",
                description = "Fasting increases brain-derived neurotrophic factor (BDNF), which helps improve memory and learning. Stay sharp! 🧠",
                emoji = "🤓",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Inflammation Fighter",
                description = "Studies show fasting can reduce inflammation, helping with conditions like arthritis and asthma. Bye-bye, aches! 👋",
                emoji = "🔥",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Immune System Reset",
                description = "Going without food for a while encourages your body to recycle old immune cells and create new ones. A natural reboot! 🔄",
                emoji = "🛡️",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Longevity Hack",
                description = "Fasting has been linked to increased lifespan in many studies. It might just help you age like fine wine! 🍷",
                emoji = "⏳",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Metabolism Booster",
                description = "Intermittent fasting can speed up metabolism and improve fat-burning efficiency. More gains, less effort! 💥",
                emoji = "🏋️",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Cell Repair Mode",
                description = "During fasting, cells repair themselves and remove harmful waste. It's like hitting the refresh button! 🔄",
                emoji = "🔧",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Hunger Hormone Tamer",
                description = "Fasting helps regulate ghrelin, the hunger hormone, making it easier to control cravings. No more snack attacks! 🍕",
                emoji = "🚫",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Heart Health Hero",
                description = "Intermittent fasting can lower blood pressure, cholesterol, and triglycerides, reducing heart disease risk. ❤️",
                emoji = "💖",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Ketone Power",
                description = "Fasting boosts ketone production, which fuels your brain and muscles for improved performance! ⚡",
                emoji = "⚡",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Insulin Sensitivity Boost",
                description = "Fasting helps regulate blood sugar and improve insulin sensitivity, lowering the risk of diabetes. 🍭",
                emoji = "📉",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Gut Rest & Reset",
                description = "Fasting gives your digestive system a break, allowing it to heal and function better. No bloating, just bliss! 🌿",
                emoji = "🍃",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Mood Enhancer",
                description = "Fasting can increase dopamine and serotonin, making you feel happier and more energized! 😊",
                emoji = "😄",
                isPulsing = false
            ),
            TimelineEntry(
                title = "More Growth Hormone",
                description = "Fasting can increase growth hormone levels by up to 5 times, aiding muscle gain and fat loss. Gains incoming! 💪",
                emoji = "🏆",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Better Sleep",
                description = "Fasting can regulate your circadian rhythm and improve sleep quality. Say hello to deep rest! 💤",
                emoji = "🌙",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Skin Glow Up",
                description = "Autophagy from fasting helps clear out damaged cells, making your skin look fresher and younger! ✨",
                emoji = "💆",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Simpler Meal Planning",
                description = "Fewer meals = less cooking and fewer dishes. Fasting saves you time and effort! 🍽️",
                emoji = "⏰",
                isPulsing = false
            ),
            TimelineEntry(
                title = "A Natural Detox",
                description = "Your liver works better when fasting, helping flush out toxins naturally. No expensive juice cleanses needed! 🥤",
                emoji = "🚿",
                isPulsing = false
            ),
            TimelineEntry(
                title = "Saves Money",
                description = "Eating less often means spending less on food. Fasting is good for your wallet too! 💰",
                emoji = "🤑",
                isPulsing = false
            )
        ).shuffled().take(3)

        _feed.value = listOf(
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

    private fun computeCongratsFeed() {
        val congratsTip = listOf(
            TimelineEntry(
                title = "Hurray!",
                description = "You've successfully completed your fast! Great job!!",
                emoji = "🎉",
                isPulsing = true
            )
        )
        _feed.value = congratsTip
    }

    fun computeHelpFeed() {
        val helpingTip = listOf(
            TimelineEntry(
                title = "Let's go!",
                description = "Hit the start button to begin!",
                emoji = "🚀",
                isPulsing = true
            )
        )
        _feed.value = helpingTip
    }

    /**
     * Computes the fasting progress and updates the timeline feed.
     */
    fun computeFeed() {
        val fast = findSelectedFast() ?: return
        val fastingStarted = fastStartDate ?: return
        val currentTimeMillis = System.currentTimeMillis()
        
         val milestoneTips = MockData.fastingMilestones.mapIndexed { index, milestone ->
         
            val fastingStartedMillis = fastingStarted.time
            val thisMilestoneMillis = fastingStartedMillis + TimeUnit.SECONDS.toMillis(milestone.timestampInSeconds.toLong())
            val nextMilestone = MockData.fastingMilestones.getOrNull(index + 1)
            val nextMilestoneMillis = nextMilestone?.let {
                fastingStartedMillis + TimeUnit.SECONDS.toMillis(it.timestampInSeconds.toLong())
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

        _feed.value = milestoneTips
    }

    /**
     * Starts the fasting countdown timer.
     */
    fun startTimer() {
        
        countDownJob = viewModelScope.launch {
            // The countdown only starts if we have a fast and we have set a fast
            if (fastStartDate != null && findSelectedFast() != null) {
                isRunning = true

                val fastingTimeInSeconds = findSelectedFast()?.fastingTimeInSeconds?.toFloat()!!
                while (true) {
                    if (findSelectedFast() == null) break
                
                    val remainingTime = computeTimeLeft()
                    _time.value = formatClockTime(remainingTime)
                    _progress.value = (1f - (remainingTime / fastingTimeInSeconds)) * 100f
                    computeFeed()
                    if (remainingTime <= 0) {
                        hasCompletedFast = true
                        break
                    }
                    delay(1000)
                }
                
                isRunning = false
            }
        }
    }

    /**
     * Toggles the fasting timer on or off.
     */
    fun toggleTimer() {
        when {
            hasCompletedFast -> stopTimer()
            isRunning -> stopTimer()
            !isRunning -> {
                if (findSelectedFast() == null) throw IllegalStateException("No fast selected!")
                fastStartDate = Date()
                hasCompletedFast = false
                startTimer()
            }
        }
    }

    /**
     * Stops the fasting timer and resets progress.
     */
    fun stopTimer() {
        countDownJob?.cancel()
        isRunning = false
        hasCompletedFast = false
        fastStartDate = null
        fastId = ""
        _time.value = formatClockTime(0)
        _progress.value = 0f
    }

    /**
     * Resumes the fasting timer if a fast is in progress.
     */
    fun resumeFast() {
        when {
            // A. Has fully completed a fast
            hasCompletedFast -> {
                computeCongratsFeed()
            }
            
            // B. Has selected a fast technique and the clock is running
            isRunning && findSelectedFast() != null -> {
                isFirstTime = false
                startTimer()
            }

            // C. We just selected a Fast
            !isRunning && findSelectedFast() != null -> {
                computeHelpFeed()
            }

            // D. Is not running and it is the first time
            !isRunning && isFirstTime -> {
                computeWelcomeFeed()
            }

            // E. It is not running
            !isRunning && !isFirstTime -> {
                computeDefaultFeed()
            }
        }
    }

    /**
     * Formats the remaining time in HH:MM:SS format.
     */
    private fun formatClockTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        return "%02d:%02d".format(hours, minutes)
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
}
