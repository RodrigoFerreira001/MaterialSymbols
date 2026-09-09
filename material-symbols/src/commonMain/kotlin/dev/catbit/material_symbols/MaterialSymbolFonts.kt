package dev.catbit.material_symbols

import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.catbit.material_symbols.resources.Res
import dev.catbit.material_symbols.resources.materialSymbolsOutlined
import dev.catbit.material_symbols.resources.materialSymbolsRounded
import dev.catbit.material_symbols.resources.materialSymbolsSharp
import org.jetbrains.compose.resources.Font

/**
 * `CompositionLocal` carrying the [MaterialSymbolFonts] loaded by [rememberMaterialSymbolFonts] —
 * provided by [MaterialSymbolsRenderingScope], read by [MaterialSymbol]. Reading it before it's
 * provided throws, which is why every glyph must be composed inside a
 * [MaterialSymbolsRenderingScope].
 */
internal val LocalMaterialSymbolFonts = staticCompositionLocalOf<MaterialSymbolFonts> {
    error("MaterialSymbolFonts not provided")
}

/**
 * The 6 loaded [Material Symbols](https://fonts.google.com/icons) font family variants — one per
 * (Outlined/Rounded/Sharp) × (regular/filled) combination — built once by
 * [rememberMaterialSymbolFonts] from a single [MaterialSymbolFontsConfig] and shared by every
 * [MaterialSymbol] glyph in the composition via [LocalMaterialSymbolFonts]. Use [resolve] to pick
 * the right variant for a given [MaterialSymbolStyle]/`filled` combination instead of matching on
 * these properties directly.
 */
@Stable
data class MaterialSymbolFonts(
    val outlined: FontFamily,
    val outlinedFilled: FontFamily,
    val rounded: FontFamily,
    val roundedFilled: FontFamily,
    val sharp: FontFamily,
    val sharpFilled: FontFamily
)

/** Picks the [FontFamily] variant matching [style] and [filled] out of this [MaterialSymbolFonts]. */
internal fun MaterialSymbolFonts.resolve(style: MaterialSymbolStyle, filled: Boolean): FontFamily =
    when (style) {
        MaterialSymbolStyle.OUTLINED -> if (filled) outlinedFilled else outlined
        MaterialSymbolStyle.ROUNDED -> if (filled) roundedFilled else rounded
        MaterialSymbolStyle.SHARP -> if (filled) sharpFilled else sharp
    }

/**
 * Variable-font axis configuration applied uniformly to every [MaterialSymbol] glyph rendered
 * inside a given [MaterialSymbolsRenderingScope]. Compose bakes variable-font axis settings into
 * the [FontFamily] itself rather than resolving them per-glyph at draw time, so all glyphs sharing
 * a scope necessarily share the same axis values.
 *
 * @property weight glyph stroke weight, Google's `wght` axis. Must resolve to a value in
 * `[100, 700]`.
 * @property grade fine weight adjustment independent of [weight], Google's `GRAD` axis. Must be in
 * `[-25, 200]`. Unlike [weight], grade doesn't change the glyph's overall size, making it suited
 * for adjustments driven by ambient light or visual emphasis rather than layout.
 * @property opticalSize the font's optical-size axis (`opsz`), in sp — larger sizes render
 * slightly bolder, thicker strokes for legibility. Must be in `[20sp, 48sp]`.
 */
@Stable
data class MaterialSymbolFontsConfig(
    val weight: FontWeight = FontWeight.Normal,
    val grade: Int = 0,
    val opticalSize: TextUnit = 24.sp
)

/**
 * Builds the 6 [MaterialSymbolFonts] variants from [config]'s variable-font axis settings — called
 * once by [MaterialSymbolsRenderingScope] to populate [LocalMaterialSymbolFonts]. The
 * [FontVariation.Settings] themselves are memoized on [config] (cheap, purely synchronous); actually
 * loading the fonts into [FontFamily]s is delegated to [rememberMaterialSymbolFontFamilies], which
 * differs per platform — see its docs for why.
 *
 * @param config the axis values to apply.
 * @throws IllegalArgumentException if any of [config]'s values falls outside its documented range.
 */
@Composable
internal fun rememberMaterialSymbolFonts(
    config: MaterialSymbolFontsConfig
): MaterialSymbolFonts {

    val (settings, settingsFilled) = remember(config) {
        require(config.weight.weight in 100..700) {
            "weight must be in [100, 700]"
        }

        require(config.grade in -25..200) {
            "grande must be in [-25, 200]"
        }

        require(config.opticalSize.value in 20f..48f) {
            "opticalSize must be in [20.sp, 48.sp]"
        }

        val settings = FontVariation.Settings(
            FontVariation.Setting("FILL", 0f),
            FontVariation.grade(config.grade),
            FontVariation.Setting("opsz", config.opticalSize.value),
            FontVariation.weight(config.weight.weight)
        )
        val settingsFilled = FontVariation.Settings(
            FontVariation.Setting("FILL", 1f),
            FontVariation.grade(config.grade),
            FontVariation.Setting("opsz", config.opticalSize.value),
            FontVariation.weight(config.weight.weight)
        )

        settings to settingsFilled
    }

    return rememberMaterialSymbolFontFamilies(settings, settingsFilled)
}

/**
 * Platform-specific loading of the 6 [MaterialSymbolFonts] variants for [settings]/[settingsFilled].
 *
 * On JVM/Android/iOS this just calls [Font] directly (see [rememberDefaultMaterialSymbolFontFamilies])
 * — those load synchronously from the bundled resources, so the very first composition already has
 * the final result. On web targets, [Font] loads asynchronously over the network instead; the actual
 * implementation there uses Compose's `preloadFont` so the returned [MaterialSymbolFonts] only
 * reflects a fully-loaded font, avoiding a flash of un-styled/fallback text (FOUT) on first paint.
 *
 * Whatever the platform, this must **not** be wrapped in an outer `remember` keyed on something that
 * stays stable across the async load (like [settings] itself) — that would freeze the result at
 * whatever the first composition produced and never observe the font finishing loading.
 */
@Composable
internal expect fun rememberMaterialSymbolFontFamilies(
    settings: FontVariation.Settings,
    settingsFilled: FontVariation.Settings
): MaterialSymbolFonts

/** Shared by every [rememberMaterialSymbolFontFamilies] `actual` that loads synchronously (i.e.
 * every platform except web) — [Font] already returns the final result on the first composition
 * there, so no extra async-awareness is needed. */
@Composable
internal fun rememberDefaultMaterialSymbolFontFamilies(
    settings: FontVariation.Settings,
    settingsFilled: FontVariation.Settings
): MaterialSymbolFonts {

    val outlined = Font(resource = Res.font.materialSymbolsOutlined, variationSettings = settings)
    val outlinedFilled = Font(resource = Res.font.materialSymbolsOutlined, variationSettings = settingsFilled)
    val rounded = Font(resource = Res.font.materialSymbolsRounded, variationSettings = settings)
    val roundedFilled = Font(resource = Res.font.materialSymbolsRounded, variationSettings = settingsFilled)
    val sharp = Font(resource = Res.font.materialSymbolsSharp, variationSettings = settings)
    val sharpFilled = Font(resource = Res.font.materialSymbolsSharp, variationSettings = settingsFilled)

    return MaterialSymbolFonts(
        outlined = FontFamily(outlined),
        outlinedFilled = FontFamily(outlinedFilled),
        rounded = FontFamily(rounded),
        roundedFilled = FontFamily(roundedFilled),
        sharp = FontFamily(sharp),
        sharpFilled = FontFamily(sharpFilled)
    )
}