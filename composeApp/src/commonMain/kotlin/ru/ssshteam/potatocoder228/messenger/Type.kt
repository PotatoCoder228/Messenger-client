package ru.ssshteam.potatocoder228.messenger

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import messenger.composeapp.generated.resources.NotoSans_Regular
import messenger.composeapp.generated.resources.NotoSans_SemiBold
import messenger.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

// Default Material 3 typography values
val baseline = Typography()


val displayFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.NotoSans_Regular))

val headlineFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.NotoSans_SemiBold))

val titleFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.NotoSans_SemiBold))

val labelFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.NotoSans_SemiBold))


val bodyFontFamily
    @Composable // Composable is required for loading font from compose resources
    get() = FontFamily(Font(resource = Res.font.NotoSans_Regular))

// Or you can also apply font to all styles
val AppTypography
    @Composable
    get() = Typography()
//        .copy(
//        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
//        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
//        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
//        headlineLarge = baseline.headlineLarge.copy(fontFamily = headlineFontFamily),
//        headlineMedium = baseline.headlineMedium.copy(fontFamily = headlineFontFamily),
//        headlineSmall = baseline.headlineSmall.copy(fontFamily = headlineFontFamily),
//        titleLarge = baseline.titleLarge.copy(fontFamily = titleFontFamily),
//        titleMedium = baseline.titleMedium.copy(fontFamily = titleFontFamily),
//        titleSmall = baseline.titleSmall.copy(fontFamily = titleFontFamily),
//        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
//        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
//        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
//        labelLarge = baseline.labelLarge.copy(fontFamily = labelFontFamily),
//        labelMedium = baseline.labelMedium.copy(fontFamily = labelFontFamily),
//        labelSmall = baseline.labelSmall.copy(fontFamily = labelFontFamily),
//    )

