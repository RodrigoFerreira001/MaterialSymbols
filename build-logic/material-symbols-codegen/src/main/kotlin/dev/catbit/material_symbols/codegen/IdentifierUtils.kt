package dev.catbit.material_symbols.codegen

/**
 * Converts a raw Material Symbols icon name (matching `[a-z0-9_]+`, e.g. `"settings"` or `"10k"`)
 * into a valid, idiomatic Kotlin constant identifier (`"SETTINGS"`, `"_10K"`).
 *
 * The only transformation needed beyond upper-casing: a small subset of Material Symbols icon
 * names (e.g. `"10k"`, `"3d_rotation"`) start with a digit, which is not a legal leading character
 * for a Kotlin identifier once upper-cased (`"10K"` is invalid) — those get an underscore prefix.
 */
internal fun iconNameToConstantName(iconName: String): String {
    val upper = iconName.uppercase()
    return if (upper.first().isDigit()) "_$upper" else upper
}
