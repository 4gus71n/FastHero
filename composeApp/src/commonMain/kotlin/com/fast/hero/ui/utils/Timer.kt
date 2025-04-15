package com.fast.hero.ui.utils


import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.fast.hero.ui.theme.FastHeroTheme
import com.fast.hero.ui.theme.materialGreen
import fasthero.composeapp.generated.resources.Res
import fasthero.composeapp.generated.resources.bg_watch
import fasthero.composeapp.generated.resources.ic_watch
import org.jetbrains.compose.resources.painterResource

private const val STEP_DELAY: Long = 150L
private const val RESTART_DELAY: Long = 3000L
const val COUNTDOWN_STEP_ANIMATION_DURATION: Int = STEP_DELAY.toInt()
const val RESTART_ANIMATION_DURATION: Int = RESTART_DELAY.toInt() - 1000
const val STOP_ANIMATION_DURATION: Int = 300

@Composable
internal fun CircularProgress(
    modifier: Modifier = Modifier,
    color: Color,
    backgroundColor: Color = color,
    startingAngle: Float = 270f,
    progress: Float,
    animate: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
) {
    val animatedProgress: Float by animateFloatAsState(targetValue = progress, animationSpec = animationSpec)
    Canvas(modifier) {
        val sweepAngle = (360 * if (animate) animatedProgress else progress) / 100
        val ringRadius = size.minDimension * 0.15f
        val size = Size(size.width, size.height)
        drawArc(backgroundColor, startingAngle, 360f, false, size = size, alpha = 0.2f, style = Stroke(ringRadius))
        drawArc(Color.Black, startingAngle, sweepAngle, false, Offset(0f, 15.dp.toPx()), size, 0.2f, Stroke(ringRadius, cap = StrokeCap.Round))
        drawArc(color, startingAngle, sweepAngle, false, size = size, style = Stroke(ringRadius, cap = StrokeCap.Round))
    }
}

@Composable
internal fun CenterProgress(
    modifier: Modifier,
    progress: Float,
    elapsed: String,
    isCountingDown: Boolean,
    isRestarting: Boolean,
    isSetting: Boolean
) {
    val elapsedAlpha: Float by animateFloatAsState(if (isSetting) 0f else 1f)
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center)
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.requiredSize(250.dp),
                painter = painterResource(Res.drawable.bg_watch),
                contentDescription = null
            )
            Image(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp),
                painter = painterResource(Res.drawable.ic_watch),
                contentDescription = null
            )
            FastHeroTheme {
                Text(
                    modifier = Modifier.padding(top = 25.dp),
                    text = elapsed,
                    style = MaterialTheme.typography.displayMedium,
                    fontStyle = FontStyle.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = elapsedAlpha)
                )
            }

            CircularProgress(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.Center),
                progress = progress,
                color = interpolateColor(
                    startColor = MaterialTheme.colorScheme.primary,
                    endColor = materialGreen,
                    progress / 100f
                ),
                backgroundColor = Color.Transparent,
                animationSpec = tween(
                    durationMillis = if (isRestarting) RESTART_ANIMATION_DURATION else (if (isCountingDown) COUNTDOWN_STEP_ANIMATION_DURATION else STOP_ANIMATION_DURATION),
                    easing = if (isCountingDown) LinearEasing else FastOutSlowInEasing
                )
            )
        }
    }
}

fun interpolateColor(startColor: Color, endColor: Color, progress: Float): Color {
    val inverseProgress = 1 - progress

    val a = startColor.alpha * inverseProgress + endColor.alpha * progress
    val r = startColor.red * inverseProgress + endColor.red * progress
    val g = startColor.green * inverseProgress + endColor.green * progress
    val b = startColor.blue * inverseProgress + endColor.blue * progress

    return Color(r, g, b, a)
}

@Composable
internal fun Timer(
    modifier: Modifier,
    progress: Float,
    elapsed: String,
    isCountingDown: Boolean,
    isRestarting: Boolean,
    isSetting: Boolean,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CenterProgress(
            modifier = Modifier.fillMaxWidth(),
            progress = progress,
            elapsed = elapsed,
            isCountingDown = isCountingDown,
            isRestarting = isRestarting,
            isSetting = isSetting,
        )
    }
}