package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LimeGreenAccent,
    onPrimary = DarkGreenHeader,
    primaryContainer = DarkGreenPrimary,
    onPrimaryContainer = LimeGreenLight,
    secondary = LimeGreenAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E381A),
    onSecondaryContainer = LimeGreenLight,
    background = Color(0xFF121B15),
    surface = Color(0xFF1A261F),
    onBackground = Color(0xFFE8F0EA),
    onSurface = Color(0xFFE8F0EA),
    surfaceVariant = Color(0xFF23352A),
    onSurfaceVariant = Color(0xFFC0CEC4),
    outline = Color(0xFF3B5244)
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF117544),
    onPrimaryContainer = Color.White,
    secondary = LimeGreenAccent,
    onSecondary = DarkGreenHeader,
    secondaryContainer = LimeGreenLight,
    onSecondaryContainer = LimeGreenDark,
    background = SoftGreenBackground,
    surface = SurfaceWhite,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantGreen,
    onSurfaceVariant = TextSecondaryMuted,
    outline = BorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep Brother Mart branded green & lime look consistent
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
