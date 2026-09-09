package dev.catbit.material_symbols.sample.extensions

import androidx.compose.ui.graphics.Color
import kotlin.math.roundToInt

private val hexColorRegex = Regex("^#[0-9a-fA-F]{6}$")

fun String.toColorOrNull(): Color? {
    if (!hexColorRegex.matches(this)) return null

    val rgb = removePrefix("#").toLong(16)
    return Color(rgb or 0xFF000000L)
}

fun Color.toHexString(): String {
    val r = (red * 255f).roundToInt().coerceIn(0, 255)
    val g = (green * 255f).roundToInt().coerceIn(0, 255)
    val b = (blue * 255f).roundToInt().coerceIn(0, 255)
    return "#" + listOf(r, g, b).joinToString(separator = "") {
        it.toString(16).padStart(2, '0').uppercase()
    }
}

val Color.isPerceivedDark: Boolean
    get() = (0.299f * red + 0.587f * green + 0.114f * blue) < 0.5f
