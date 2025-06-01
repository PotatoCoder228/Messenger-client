package ru.ssshteam.potatocoder228.messenger

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import messenger.composeapp.generated.resources.Res
import messenger.composeapp.generated.resources.Roboto_Medium
import messenger.composeapp.generated.resources.Roboto_SemiBold
import org.jetbrains.compose.resources.Font

// Default Material 3 typography values
val baseline = Typography()


val displayFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.Roboto_SemiBold))


val bodyFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.Roboto_Medium))

// Or you can also apply font to all styles
val AppTypography
    @Composable
    get() = Typography().copy(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )

