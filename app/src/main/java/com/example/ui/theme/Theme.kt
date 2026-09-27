package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val LocalPastelPalette = staticCompositionLocalOf { PastelThemePalette.ROSE_WATER }

val ItGirlShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun ItGirlTheme(
    isArabic: Boolean = false,
    palette: PastelThemePalette = PastelThemePalette.ROSE_WATER,
    content: @Composable () -> Unit
) {
    val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr
    val dynamicScheme = lightColorScheme(
        primary = palette.deepAccent,
        onPrimary = CreamSurface,
        primaryContainer = palette.primaryPastel,
        onPrimaryContainer = WarmSlateBrown,
        secondary = palette.deepAccent,
        onSecondary = CreamSurface,
        secondaryContainer = palette.secondaryPastel,
        onSecondaryContainer = WarmSlateBrown,
        tertiary = DustyRoseDeep,
        onTertiary = CreamSurface,
        tertiaryContainer = palette.primaryPastel,
        onTertiaryContainer = WarmSlateBrown,
        background = palette.background,
        onBackground = WarmSlateBrown,
        surface = palette.cardSurface,
        onSurface = WarmSlateBrown,
        surfaceVariant = palette.secondaryPastel,
        onSurfaceVariant = MutedCocoa,
        outline = palette.borderTint
    )

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection,
        LocalPastelPalette provides palette
    ) {
        MaterialTheme(
            colorScheme = dynamicScheme,
            typography = getAppTypography(isArabic),
            shapes = ItGirlShapes,
            content = content
        )
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    ItGirlTheme(isArabic = false, palette = PastelThemePalette.ROSE_WATER, content = content)
}
