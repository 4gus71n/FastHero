package com.fast.hero.sdk

import app.cash.sqldelight.db.SqlDriver
import co.touchlab.kermit.Logger
import com.fast.hero.getSqlDriver
import com.fast.hero.models.FastingMetrics
import com.fast.hero.sql.UserPreferencesRepository
import com.fast.hero.ui.screens.models.TimelineEntry
import com.fast.hero.ui.screens.settings.SettingOptionList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.abs

const val IS_FASTCORE_DEBUG = true

public class FastCore(
    driver: SqlDriver,
    now: (() -> Long)
) {
    private val preferences = UserPreferencesRepository(driver)
    private val getNowInMillis = now

    private val logger = Logger.withTag("FastCore")

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var countDownJob: Job? = null
    
    private val feedGenerator = FeedGenerator()
    

    // State for the formatted time remaining (HH:MM:SS)
    private val _time = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    
    public val time: SharedFlow<String> = _time.asSharedFlow().apply {
        onEach { logger.d("Time: $it") }
    }

    // State for fasting progress (percentage)
    private val _progress = MutableSharedFlow<Float>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    public val progress: SharedFlow<Float> = _progress.asSharedFlow().apply { 
        onEach { logger.d("Progress: $it") }
    }

    public val observeIsFastRunning: SharedFlow<Boolean> = observePreferences()
        .filterNotNull()
        .map { it.isRunning != 0L }
        .distinctUntilChanged()
        .shareIn(coroutineScope, SharingStarted.Lazily, 1)

    public val observeSelectedFastId: SharedFlow<String> = observePreferences()
        .filterNotNull()
        .map { it.selectedFast }
        .distinctUntilChanged()
        .shareIn(coroutineScope, SharingStarted.Lazily, 1)
    
    private fun observePreferences() = preferences.observePreferences()

    // State flow for the feed (timeline of fasting milestones)
    private val _feed = MutableSharedFlow<List<TimelineEntry>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    public val feed: SharedFlow<List<TimelineEntry>> = _feed.asSharedFlow().apply { 
        onEach { logger.d("Feed: $it") }
    }
    

    // Gets or sets the fasting start date in shared preferences
    private var fastStartDateInMillis: Long?
        get() {
            return  preferences.getPreferences().fastStartTimeInMillis
        }
        set(value) {
            preferences.saveFastStartTimeInMillis(value)
        }
    
    // Gets or sets the fasting ID in shared preferences
    public var fastId: String
        get() = findSelectedFast()?.id ?: ""
        set(value) {
            preferences.saveSelectedFast(value)
        }

    // Gets or sets the fasting timer state (running or stopped)
    private var isRunning: Boolean
        get() = preferences.getPreferences().isRunning
        set(value) {
            preferences.saveIsRunning(value)
        }

    private var hasCompletedFast: Boolean
        get() = preferences.getPreferences().hasCompletedFast
        set(value) {
            preferences.saveHasCompletedFast(value)
        }

    // Tells if the app has been installed for the first time
    private var isFirstTime: Boolean
        get() = preferences.getPreferences().isFirstTime
        set(value) {
            preferences.saveIsFirstTime(value)
        }
    
    
    public fun findSelectedFast(): SettingOptionList? {
        return preferences.getPreferences().selectedFast.let { selectedFast ->
            MockData.fastingOptions.find { it.id == selectedFast }
        }
    }

    private val wizardMatrix = listOfNotNull(
        if (IS_FASTCORE_DEBUG) {
            FastingMetrics(id = "TEST", difficulty = 2, compromise = 9, doability = 10)
        } else {
            null
        },
        FastingMetrics(id = "12:12", difficulty = 2, compromise = 9, doability = 10),
        FastingMetrics(id = "14:10", difficulty = 4, compromise = 2, doability = 9),
        FastingMetrics(id = "16:8", difficulty = 5, compromise = 3, doability = 8),
        FastingMetrics(id = "18:6", difficulty = 7, compromise = 4, doability = 6),
        FastingMetrics(id = "20:4", difficulty = 8, compromise = 6, doability = 4),
        FastingMetrics(id = "OMAD", difficulty = 9, compromise = 7, doability = 3),
        FastingMetrics(id = "14:10 Crescendo", difficulty = 7, compromise = 1, doability = 9)
    )

    public fun sortRecommendationList(
        difficulty: Int,
        compromise: Int,
        doability: Int
    ): List<SettingOptionList> {
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

        return matchingScores
            .sortedBy { it.second }
            .mapNotNull { pair -> fastingOptions.find { f -> f.id == pair.first } }

    }

    private fun startTimer() {
        isRunning = true
        countDownJob = coroutineScope.launch {
            val selectedFast = findSelectedFast()
            val startTime = fastStartDateInMillis
            if (selectedFast == null || startTime == null) {
                logger.d("Fast stopped! selectedFast = $selectedFast startTime = $startTime")
                stopTimer()
            } else {
                val totalSeconds = selectedFast.fastingTimeInSeconds.toFloat()
                while (isRunning) {
                    val elapsed = (getNowInMillis() - startTime) / 1000f
                    val remaining = (totalSeconds - elapsed).coerceAtLeast(0f)

                    _time.tryEmit(
                        formatClockTime(remaining.toLong())
                    )
                    _progress.tryEmit(
                        ((elapsed / totalSeconds) * 100f).coerceIn(0f, 100f)
                    )
                    _feed.tryEmit(
                        feedGenerator.computeFeed(
                            currentTimeMillis = getNowInMillis(),
                            fastStartDateInMillis = fastStartDateInMillis
                        )
                    ) 

                    if (remaining <= 0f) {
                        logger.d("Fast completed! remaining = $remaining")
                        hasCompletedFast = true
                        break
                    }

                    delay(1000)
                }
            }
            isRunning = false
        }
    }

    /**
     * Toggles the fasting timer on or off.
     */
    public fun toggleTimer() {
        logger.d("Call toggleTimer")
        when {
            hasCompletedFast -> stopTimer()
            isRunning -> stopTimer()
            !isRunning -> {
                if (findSelectedFast() == null) throw IllegalStateException("No fast selected!")
                fastStartDateInMillis = getNowInMillis()
                hasCompletedFast = false
                startTimer()
            }
        }
    }

    /**
     * Stops the fasting timer and resets progress.
     */
    public fun stopTimer() {
        logger.d("Call stopTimer")
        countDownJob?.cancel()
        isRunning = false
        hasCompletedFast = false
        fastStartDateInMillis = null
        fastId = ""
        _time.tryEmit(
            formatClockTime(0)
        )
        _progress.tryEmit(
            0f
        )
    }

    private fun formatClockTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return "${padWithZero(hours)}:${padWithZero(minutes)}:${padWithZero(secs)}"
    }

    private fun padWithZero(value: Long): String {
        return if (value < 10) "0$value" else value.toString()
    }

    /**
     * Resumes the fasting timer if a fast is in progress.
     */
    public fun resumeFast() {
        when {
            hasCompletedFast -> {
                logger.d("Resume Fast > Has fully completed a fast")
                _feed.tryEmit(feedGenerator.computeCongratsFeed())
            }
            isRunning && fastId.isNotBlank() -> {
                logger.d("Resume Fast > Has selected a fast technique and the clock is running")
                isFirstTime = false
                startTimer()
            }
            !isRunning && fastId.isNotBlank() -> {
                logger.d("Resume Fast > We just selected a Fast")
                _feed.tryEmit(feedGenerator.computeHelpFeed())
            }
            !isRunning && isFirstTime -> {
                logger.d("Resume Fast > Is not running and it is the first time")
                _feed.tryEmit(feedGenerator.computeWelcomeFeed())
            }
            !isRunning && !isFirstTime -> {
                logger.d("Resume Fast > It is not running")
                _feed.tryEmit(feedGenerator.computeDefaultFeed())
            }
        }

        val selectedFast = findSelectedFast()
        val startTime = fastStartDateInMillis
        if (selectedFast != null && startTime != null) {
            val totalSeconds = selectedFast.fastingTimeInSeconds.toFloat()
            val elapsed = (getNowInMillis() - startTime) / 1000f
            val remaining = (totalSeconds - elapsed).coerceAtLeast(0f)
            _time.tryEmit(
                formatClockTime(remaining.toLong())
            )
            _progress.tryEmit(
                ((elapsed / totalSeconds) * 100f).coerceIn(0f, 100f)
            )
        } else {
            _time.tryEmit("--:--")
            _progress.tryEmit(0f)
        }
    }
}