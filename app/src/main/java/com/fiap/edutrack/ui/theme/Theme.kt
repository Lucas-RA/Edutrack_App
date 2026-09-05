package com.fiap.edutrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EduTrackColorScheme = lightColorScheme(
    primary = Green800,
    onPrimary = Ink50,
    primaryContainer = Green100,
    onPrimaryContainer = Green900,
    secondary = BrandTeal,
    onSecondary = Ink50,
    tertiary = BrandBlue,
    background = Ink50,
    onBackground = BrandDeep,
    surface = Color.White,
    onSurface = BrandDeep,
    error = Red500
)

@Composable
fun EduTrackTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EduTrackColorScheme,
        typography = EduTrackTypography,
        shapes = EduTrackShapes,
        content = content
    )
}
