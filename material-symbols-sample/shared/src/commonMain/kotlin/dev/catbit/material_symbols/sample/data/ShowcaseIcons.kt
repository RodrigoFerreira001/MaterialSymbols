package dev.catbit.material_symbols.sample.data

import dev.catbit.material_symbols.sample.shared.resources.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * One entry from Google's [Material Symbols](https://fonts.google.com/icons) catalog. [name]
 * is the raw icon name usable directly with
 * [dev.catbit.material_symbols.MaterialSymbol]/[dev.catbit.material_symbols.MaterialSymbols];
 * [label] is a human-readable version of it for display (e.g. `"arrow_back"` -> `"Arrow Back"`,
 * `"10k"` -> `"10k"` since there's no letter to capitalize).
 *
 * @property category the icon's primary category on the Material Symbols site (e.g. `"Maps"`).
 * Icons can belong to more than one category upstream; only the first is kept here.
 * @property tags search keywords Google associates with the icon.
 * @property popularity Google's own popularity ranking for the icon (higher sorts first there).
 */
data class MaterialSymbolInstance(
    val label: String,
    val name: String,
    val category: String,
    val tags: List<String>,
    val popularity: Int
)

/**
 * Every icon in Google's [Material Symbols](https://fonts.google.com/icons) catalog available in
 * all 3 bundled font styles (Outlined/Rounded/Sharp) — parsed at runtime from the raw metadata
 * snapshot bundled as a Compose resource (`files/materialSymbols.json`, the same response
 * `https://fonts.google.com/metadata/icons?key=material_symbols&incomplete=true` returns). Powers
 * the sample app's icon showcase; not part of the published `material-symbols` library itself,
 * since shipping every icon's category/tags/popularity would bloat it for no reason consumers need.
 */
object ShowcaseIcons {

    private val MATERIAL_SYMBOLS_FAMILIES = setOf(
        "Material Symbols Outlined",
        "Material Symbols Rounded",
        "Material Symbols Sharp"
    )

    private val json = Json { ignoreUnknownKeys = true }

    private var cached: List<MaterialSymbolInstance>? = null

    suspend fun load(): List<MaterialSymbolInstance> {
        cached?.let { return it }

        val bytes = Res.readBytes("files/materialSymbols.json")
        val rawJson = bytes.decodeToString().substringAfter('\n')
        val metadata = json.decodeFromString(RawMetadata.serializer(), rawJson)

        val icons = metadata.icons
            .asSequence()
            .filter { icon -> MATERIAL_SYMBOLS_FAMILIES.none { it in icon.unsupportedFamilies } }
            .distinctBy { it.name }
            .map { icon ->
                MaterialSymbolInstance(
                    label = icon.name.toLabel(),
                    name = icon.name,
                    category = icon.categories.firstOrNull().orEmpty(),
                    tags = icon.tags,
                    popularity = icon.popularity
                )
            }
            .sortedBy { it.name }
            .toList()

        cached = icons
        return icons
    }

    private fun String.toLabel(): String =
        split('_').joinToString(" ") { part ->
            if (part.isNotEmpty() && part[0].isLetter()) {
                part.replaceFirstChar { it.uppercase() }
            } else {
                part
            }
        }
}

@Serializable
private data class RawMetadata(
    @SerialName("icons") val icons: List<RawIcon>
)

@Serializable
private data class RawIcon(
    @SerialName("name") val name: String,
    @SerialName("unsupported_families") val unsupportedFamilies: List<String> = emptyList(),
    @SerialName("categories") val categories: List<String> = emptyList(),
    @SerialName("tags") val tags: List<String> = emptyList(),
    @SerialName("popularity") val popularity: Int = 0
)
