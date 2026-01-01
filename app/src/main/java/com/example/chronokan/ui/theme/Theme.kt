package com.example.chronokan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ChronosBlue,
    secondary = ChronosAccent,
    surface = ChronosBlack,
    surfaceVariant = ChronosDarkGrey,
    background = ChronosBlack
)

@Composable
fun ChronosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}