package dev.catbit.material_symbols

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

/**
 * Loads the [Material Symbols](https://fonts.google.com/icons) variable fonts described by
 * [config] and provides them to [content] via [LocalMaterialSymbolFonts], so every [MaterialSymbol]
 * composed underneath can render without reloading its fonts. Wrap the part of your UI tree that
 * draws Material Symbols glyphs in this scope — typically once, near the root of your app.
 *
 * Font loading happens once per distinct [config] and is [remember]ed across recompositions; it is
 * not redone just because [content] recomposes.
 *
 * @param config the variable-font axis values ([weight][MaterialSymbolFontsConfig.weight],
 * [grade][MaterialSymbolFontsConfig.grade], [opticalSize][MaterialSymbolFontsConfig.opticalSize])
 * shared by every glyph rendered inside [content]. Defaults to [MaterialSymbolFontsConfig]'s
 * defaults.
 * @param content the composable content that may render [MaterialSymbol] glyphs.
 * @throws IllegalArgumentException if any of [config]'s values falls outside its documented range.
 */
@Composable
fun MaterialSymbolsRenderingScope(
    config: MaterialSymbolFontsConfig = remember { MaterialSymbolFontsConfig() },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        value = LocalMaterialSymbolFonts provides rememberMaterialSymbolFonts(config),
        content = content
    )
}