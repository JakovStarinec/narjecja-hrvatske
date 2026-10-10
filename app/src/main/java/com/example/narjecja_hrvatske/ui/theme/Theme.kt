package com.example.narjecja_hrvatske.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Rust50,
    onPrimary = Color.White,
    primaryContainer = Rust20,
    onPrimaryContainer = Rust80,

    secondary = Taupe50,
    onSecondary = Color.White,
    secondaryContainer = Cream30,
    onSecondaryContainer = Espresso90,

    tertiary = Gold40,
    onTertiary = Gold80,
    tertiaryContainer = Gold20,
    onTertiaryContainer = Gold80,

    background = Cream20,
    onBackground = Espresso90,

    surface = Color.White,
    onSurface = Espresso90,
    surfaceVariant = Cream30,
    onSurfaceVariant = Taupe50,

    outline = Taupe20,
    outlineVariant = Taupe20,

    error = Danger50,
    onError = Color.White,
    errorContainer = Danger10,
    onErrorContainer = Danger80
)

// Pripremljeno za budući sustavski dark mode - trenutno se ne koristi automatski
// jer bi zahtijevao i posebno ugođene boje po ekranima, ne samo shemu.
private val DarkColorScheme = darkColorScheme(
    primary = RustDark80,
    onPrimary = Espresso90,
    primaryContainer = Rust60,
    onPrimaryContainer = Rust20,

    secondary = Taupe30,
    onSecondary = Espresso90,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = Cream20,

    tertiary = Gold40,
    onTertiary = Espresso90,
    tertiaryContainer = Gold80,
    onTertiaryContainer = Gold20,

    background = DarkBackground,
    onBackground = Cream20,

    surface = DarkSurface,
    onSurface = Cream20,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Taupe30,

    outline = Espresso70,
    outlineVariant = Espresso70,

    error = Danger50,
    onError = Espresso90,
    errorContainer = Danger80,
    onErrorContainer = Danger10
)

@Composable
fun Narjecja_HrvatskeTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
