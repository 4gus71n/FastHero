package com.fast.hero.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


// import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dev.datlag.tooling.compose.toTypography
import fasthero.composeapp.generated.resources.Manrope_extra_light
import fasthero.composeapp.generated.resources.Manrope_extra_light_italic
import fasthero.composeapp.generated.resources.Manrope_bold
import fasthero.composeapp.generated.resources.Manrope_bold_italic
import fasthero.composeapp.generated.resources.Manrope_extra_bold
import fasthero.composeapp.generated.resources.Manrope_extra_bold_italic
import fasthero.composeapp.generated.resources.Manrope_light
import fasthero.composeapp.generated.resources.Manrope_light_italic
import fasthero.composeapp.generated.resources.Manrope_medium
import fasthero.composeapp.generated.resources.Manrope_medium_italic
import fasthero.composeapp.generated.resources.Manrope_regular
import fasthero.composeapp.generated.resources.Manrope_regular_italic
import fasthero.composeapp.generated.resources.Manrope_semi_bold
import fasthero.composeapp.generated.resources.Manrope_semi_bold_italic

import fasthero.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Composable
fun FastHeroTheme(
    content: @Composable() () -> Unit
) {
  MaterialTheme(
    colorScheme = darkScheme,
    typography = ManropeFontFamily().toTypography(),
    content = content
  )
}


@Composable
fun ManropeFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.Manrope_extra_light, FontWeight.ExtraLight),
        Font(Res.font.Manrope_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),

        Font(Res.font.Manrope_light, FontWeight.Light),
        Font(Res.font.Manrope_light_italic, FontWeight.Light, FontStyle.Italic),

        Font(Res.font.Manrope_regular, FontWeight.Normal),
        Font(Res.font.Manrope_regular_italic, FontWeight.Normal, FontStyle.Italic),

        Font(Res.font.Manrope_medium, FontWeight.Medium),
        Font(Res.font.Manrope_medium_italic, FontWeight.Medium, FontStyle.Italic),

        Font(Res.font.Manrope_semi_bold, FontWeight.SemiBold),
        Font(Res.font.Manrope_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),

        Font(Res.font.Manrope_bold, FontWeight.Bold),
        Font(Res.font.Manrope_bold_italic, FontWeight.Bold, FontStyle.Italic),

        Font(Res.font.Manrope_extra_bold, FontWeight.ExtraBold),
        Font(Res.font.Manrope_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    )
}
