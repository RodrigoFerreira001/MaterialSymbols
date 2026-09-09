package dev.catbit.material_symbols

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import dev.catbit.material_symbols.resources.Res
import dev.catbit.material_symbols.resources.materialSymbolsOutlined
import dev.catbit.material_symbols.resources.materialSymbolsRounded
import dev.catbit.material_symbols.resources.materialSymbolsSharp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont

/** Uses `preloadFont` instead of plain [org.jetbrains.compose.resources.Font] — on web, [Font]
 * loads asynchronously, and `preloadFont` gives us a [androidx.compose.runtime.State] we can read
 * directly (`null` until loaded) rather than a [androidx.compose.ui.text.font.Font] whose readiness
 * isn't otherwise observable, so [FontFamily.Default] is used as a placeholder until each font
 * finishes loading — no un-styled/ligature-text flash once the recomposition triggered by the state
 * update lands. */
@OptIn(ExperimentalResourceApi::class)
@Composable
internal actual fun rememberMaterialSymbolFontFamilies(
    settings: FontVariation.Settings,
    settingsFilled: FontVariation.Settings
): MaterialSymbolFonts {

    val outlined by preloadFont(resource = Res.font.materialSymbolsOutlined, variationSettings = settings)
    val outlinedFilled by preloadFont(resource = Res.font.materialSymbolsOutlined, variationSettings = settingsFilled)
    val rounded by preloadFont(resource = Res.font.materialSymbolsRounded, variationSettings = settings)
    val roundedFilled by preloadFont(resource = Res.font.materialSymbolsRounded, variationSettings = settingsFilled)
    val sharp by preloadFont(resource = Res.font.materialSymbolsSharp, variationSettings = settings)
    val sharpFilled by preloadFont(resource = Res.font.materialSymbolsSharp, variationSettings = settingsFilled)

    return MaterialSymbolFonts(
        outlined = outlined?.let { FontFamily(it) } ?: FontFamily.Default,
        outlinedFilled = outlinedFilled?.let { FontFamily(it) } ?: FontFamily.Default,
        rounded = rounded?.let { FontFamily(it) } ?: FontFamily.Default,
        roundedFilled = roundedFilled?.let { FontFamily(it) } ?: FontFamily.Default,
        sharp = sharp?.let { FontFamily(it) } ?: FontFamily.Default,
        sharpFilled = sharpFilled?.let { FontFamily(it) } ?: FontFamily.Default
    )
}
