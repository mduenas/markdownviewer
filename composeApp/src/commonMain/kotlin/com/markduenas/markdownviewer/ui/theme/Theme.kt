package com.markduenas.markdownviewer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Orange seed color as specified in SPECIFICATION.md
private val OrangeSeed = Color(0xFFFF6D00)

// Light color scheme derived from orange seed
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFB35400),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDCC5),
    onPrimaryContainer = Color(0xFF3A1800),
    secondary = Color(0xFF755846),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDCC5),
    onSecondaryContainer = Color(0xFF2B1708),
    tertiary = Color(0xFF5E6135),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE4E6AE),
    onTertiaryContainer = Color(0xFF1B1D00),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF4DED3),
    onSurfaceVariant = Color(0xFF52443C),
    outline = Color(0xFF85746B),
    outlineVariant = Color(0xFFD7C3B8),
    inverseSurface = Color(0xFF362F2B),
    inverseOnSurface = Color(0xFFFBEEE8),
    inversePrimary = Color(0xFFFFB782),
)

// Dark color scheme derived from orange seed
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB782),
    onPrimary = Color(0xFF5D2C00),
    primaryContainer = Color(0xFF843E00),
    onPrimaryContainer = Color(0xFFFFDCC5),
    secondary = Color(0xFFE5BFA8),
    onSecondary = Color(0xFF432B1B),
    secondaryContainer = Color(0xFF5C4130),
    onSecondaryContainer = Color(0xFFFFDCC5),
    tertiary = Color(0xFFC7CA94),
    onTertiary = Color(0xFF30330B),
    tertiaryContainer = Color(0xFF474A20),
    onTertiaryContainer = Color(0xFFE4E6AE),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF201A17),
    onBackground = Color(0xFFECE0DA),
    surface = Color(0xFF201A17),
    onSurface = Color(0xFFECE0DA),
    surfaceVariant = Color(0xFF52443C),
    onSurfaceVariant = Color(0xFFD7C3B8),
    outline = Color(0xFF9F8D83),
    outlineVariant = Color(0xFF52443C),
    inverseSurface = Color(0xFFECE0DA),
    inverseOnSurface = Color(0xFF362F2B),
    inversePrimary = Color(0xFFB35400),
)

@Composable
fun MarkdownViewerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MarkdownViewerTypography,
        content = content
    )
}
