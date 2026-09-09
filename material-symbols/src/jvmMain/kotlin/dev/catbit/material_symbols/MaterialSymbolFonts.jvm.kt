package dev.catbit.material_symbols

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontVariation

@Composable
internal actual fun rememberMaterialSymbolFontFamilies(
    settings: FontVariation.Settings,
    settingsFilled: FontVariation.Settings
): MaterialSymbolFonts = rememberDefaultMaterialSymbolFontFamilies(settings, settingsFilled)
