package dev.catbit.material_symbols

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** [MaterialSymbol]'s default size when none is given — Material's standard icon size, matching
 * [androidx.compose.material3.Icon]'s own `SmallIconButtonTokens.IconSize` default. */
private val DefaultMaterialSymbolSize = 24.dp

/**
 * Renders a single [Material Symbol](https://fonts.google.com/icons) glyph using the variable
 * icon fonts provided by [LocalMaterialSymbolFonts] — every glyph axis (fill, weight, grade,
 * optical size) is baked into the [FontFamily][androidx.compose.ui.text.font.FontFamily] chosen
 * by [style]/[filled], so drawing an icon here is as cheap as drawing a single character of text.
 *
 * This is the bottom-level primitive of this library — a thin wrapper around [Text] that renders
 * exactly one glyph and nothing else.
 *
 * ### Accessibility
 * The rendered glyph is a private-use-area code point from an icon font, not human-readable text,
 * so this composable always replaces its default text semantics with [Role.Image] via
 * [Modifier.clearAndSetSemantics][androidx.compose.ui.semantics.clearAndSetSemantics]. Passing
 * [contentDescription] attaches a label an accessibility service will announce; passing `null`
 * marks the glyph as purely decorative and hides it from the accessibility tree entirely — mirror
 * this decision the same way you would for [androidx.compose.material3.Icon].
 *
 * ### Requirements
 * Must be composed underneath a provider of [LocalMaterialSymbolFonts] (e.g.
 * [MaterialSymbolsRenderingScope]) — reading it before it's provided throws.
 *
 * @param iconName the Material Symbol's icon name, exactly as it appears on the
 * [Material Symbols catalog](https://fonts.google.com/icons) (e.g. `"settings"`, `"home"`,
 * `"arrow_back"`). Prefer a constant from the generated [MaterialSymbols] catalog (e.g.
 * [MaterialSymbols.SETTINGS]) over a hand-typed string — it's built from the same catalog at
 * build time, so it can't drift from a valid icon name the way a raw string literal can.
 * @param contentDescription a label describing this icon for accessibility services, or `null` if
 * the icon is purely decorative and conveys no information on its own (for example, when it sits
 * next to text that already says the same thing).
 * @param modifier applied to the underlying [Text] that draws the glyph.
 * @param style which of the 3 bundled Material Symbols families to draw from — Outlined, Rounded,
 * or Sharp. Defaults to [MaterialSymbolStyle.OUTLINED].
 * @param filled whether to render the filled variant of the glyph rather than its outline weight.
 * Defaults to `false`.
 * @param size the glyph's size. When `null`, falls back to [DefaultMaterialSymbolSize] (24dp) —
 * Material's standard icon size, matching [androidx.compose.material3.Icon]'s own default — rather
 * than the ambient [LocalTextStyle][androidx.compose.material3.LocalTextStyle], so this renders at
 * the expected size inside `Button`/`IconButton`/etc. without needing to be sized manually.
 * @param tint the glyph's color. When `null`, falls back to
 * [LocalContentColor][androidx.compose.material3.LocalContentColor].
 */
@Composable
fun MaterialSymbol(
    iconName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: MaterialSymbolStyle = MaterialSymbolStyle.OUTLINED,
    filled: Boolean = false,
    size: Dp? = null,
    tint: Color? = null
) {
    val materialSymbolFonts = LocalMaterialSymbolFonts.current
    val fontFamily = materialSymbolFonts.resolve(style, filled)

    val fontSize = with(LocalDensity.current) { (size ?: DefaultMaterialSymbolSize).toSp() }
    val tint = tint ?: LocalContentColor.current

    Text(
        modifier = modifier.clearAndSetSemantics {
            role = Role.Image
            if (contentDescription != null) {
                this.contentDescription = contentDescription
            }
        },
        text = iconName,
        fontFamily = fontFamily,
        fontSize = fontSize,
        lineHeight = fontSize,
        color = tint
    )
}

/**
 * Which of the 3 [Material Symbols](https://fonts.google.com/icons) font families a glyph draws
 * from. Google publishes the icon set in three visually distinct styles that share the same icon
 * names and variable-font axes ([androidx.compose.ui.text.font.FontVariation]) — only the shape of
 * the strokes differs.
 */
enum class MaterialSymbolStyle {
    /** Rounded-corner strokes with squared terminals — the default Material Symbols style. */
    OUTLINED,

    /** Fully rounded strokes and terminals, for a softer look. */
    ROUNDED,

    /** Sharp, right-angled strokes and terminals, for a more geometric look. */
    SHARP
}

/** One [MaterialSymbol] call plus the [MaterialSymbolFontsConfig] its [MaterialSymbolsRenderingScope]
 * should be loaded with — grouped together since a [MaterialSymbolFontsConfig] change requires
 * reloading the underlying fonts, unlike the rest of [MaterialSymbol]'s parameters. */
private data class MaterialSymbolPreviewCase(
    val label: String,
    val iconName: String,
    val style: MaterialSymbolStyle = MaterialSymbolStyle.OUTLINED,
    val filled: Boolean = false,
    val size: Dp? = null,
    val tint: Color? = null,
    val fontsConfig: MaterialSymbolFontsConfig = MaterialSymbolFontsConfig()
)

private class MaterialSymbolPreviewParameterProvider : PreviewParameterProvider<MaterialSymbolPreviewCase> {

    override val values = sequenceOf(
        MaterialSymbolPreviewCase(
            label = "Defaults",
            iconName = MaterialSymbols.SETTINGS
        ),
        MaterialSymbolPreviewCase(
            label = "Filled, tinted, larger",
            iconName = MaterialSymbols.FAVORITE,
            filled = true,
            size = 40.dp,
            tint = Color(0xFFB3261E)
        ),
        MaterialSymbolPreviewCase(
            label = "Rounded, small",
            iconName = MaterialSymbols.HOME,
            style = MaterialSymbolStyle.ROUNDED,
            size = 16.dp,
            tint = Color(0xFF6750A4)
        ),
        MaterialSymbolPreviewCase(
            label = "Sharp, filled",
            iconName = MaterialSymbols.SEARCH,
            style = MaterialSymbolStyle.SHARP,
            filled = true,
            size = 32.dp
        ),
        MaterialSymbolPreviewCase(
            label = "Light weight, negative grade",
            iconName = MaterialSymbols.ARROW_BACK,
            size = 32.dp,
            fontsConfig = MaterialSymbolFontsConfig(
                weight = FontWeight.Light,
                grade = -25,
                opticalSize = 20.sp
            )
        ),
        MaterialSymbolPreviewCase(
            label = "Bold weight, high grade, large optical size",
            iconName = MaterialSymbols.NOTIFICATIONS,
            filled = true,
            size = 32.dp,
            fontsConfig = MaterialSymbolFontsConfig(
                weight = FontWeight.Bold,
                grade = 200,
                opticalSize = 48.sp
            )
        ),
        MaterialSymbolPreviewCase(
            label = "Max weight axis value",
            iconName = MaterialSymbols.STAR,
            filled = true,
            size = 32.dp,
            tint = Color(0xFFFFB300),
            fontsConfig = MaterialSymbolFontsConfig(
                weight = FontWeight(700),
                grade = 0,
                opticalSize = 36.sp
            )
        )
    )
}

/** Every case in [MaterialSymbolPreviewParameterProvider] side by side — each case is loaded under
 * its own [MaterialSymbolsRenderingScope] since [MaterialSymbolFontsConfig] is only resolved once
 * per scope. */
@Preview
@Composable
private fun MaterialSymbolPreview(
    @PreviewParameter(MaterialSymbolPreviewParameterProvider::class) case: MaterialSymbolPreviewCase
) {
    MaterialSymbolsRenderingScope(config = case.fontsConfig) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MaterialSymbol(
                iconName = case.iconName,
                contentDescription = case.label,
                style = case.style,
                filled = case.filled,
                size = case.size,
                tint = case.tint
            )
        }
    }
}
