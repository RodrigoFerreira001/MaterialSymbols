package dev.catbit.material_symbols.codegen

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Deserialization target for `https://fonts.google.com/metadata/icons` (and its bundled fallback
 * snapshot, [FALLBACK_ICONS_RESOURCE_PATH]) — only the fields the codegen task actually needs are
 * modeled; everything else in the real response is dropped via `Json { ignoreUnknownKeys = true }`. */
@Serializable
internal data class FontMetadata(
    val icons: List<Icon>
)

/**
 * @property name the icon's name, e.g. `"settings"`.
 * @property unsupportedFamilies which font families (e.g. `"Material Symbols Outlined"`) this icon
 * is *not* available in. The same [name] can appear in more than one [Icon] entry in the raw feed
 * — see [dev.catbit.material_symbols.codegen.MATERIAL_SYMBOLS_FAMILIES] for how that's handled.
 */
@Serializable
internal data class Icon(
    val name: String,
    @SerialName("unsupported_families")
    val unsupportedFamilies: List<String> = emptyList()
)
