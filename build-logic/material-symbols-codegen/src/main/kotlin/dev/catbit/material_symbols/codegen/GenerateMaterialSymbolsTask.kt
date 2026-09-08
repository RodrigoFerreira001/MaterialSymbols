package dev.catbit.material_symbols.codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.json.Json
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

private const val METADATA_ENDPOINT_URL =
    "https://fonts.google.com/metadata/icons?key=material_symbols&incomplete=true"

/** Bundled snapshot used whenever [METADATA_ENDPOINT_URL] can't be reached — same JSON shape as the
 * live response (just trimmed to the fields [Icon] actually models), so both sources are parsed and
 * filtered through the exact same code path below. */
private const val FALLBACK_ICONS_RESOURCE_PATH = "dev/catbit/material_symbols/codegen/fallback-icons.json"

/** The 3 Material Symbols families this library bundles fonts for — see
 * `library/src/commonMain/composeResources/font/`. An icon only makes it into the generated enum
 * if it's available in all 3, since [MaterialSymbol][dev.catbit.material_symbols.MaterialSymbol]
 * lets callers pick any of the 3 styles for any icon name. */
private val MATERIAL_SYMBOLS_FAMILIES = setOf(
    "Material Symbols Outlined",
    "Material Symbols Rounded",
    "Material Symbols Sharp"
)

private const val GENERATED_PACKAGE = "dev.catbit.material_symbols"
private const val GENERATED_TYPE_NAME = "MaterialSymbols"

/**
 * Generates `MaterialSymbols.kt` — an `object MaterialSymbols` with one `const val String`
 * property per icon name in Google's [Material Symbols](https://fonts.google.com/icons) catalog —
 * into [outputDirectory], sourced from [METADATA_ENDPOINT_URL] with a bundled fallback snapshot
 * for when that endpoint can't be reached (offline builds, CI without network egress, an outage,
 * or the endpoint's shape changing unexpectedly).
 *
 * This is deliberately an `object` of `const val`s rather than a Kotlin `enum class`: a real enum
 * with ~3900 entries overflows the JVM's 64KB-per-method limit, since every enum constant requires
 * runtime allocation bytecode in the class's static initializer. `const val String` properties
 * carry zero initialization bytecode at all (they're baked as `ConstantValue` field attributes),
 * so this scales to any number of icons.
 */
abstract class GenerateMaterialSymbolsTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    private val json = Json { ignoreUnknownKeys = true }

    @TaskAction
    fun generate() {
        val rawJson = fetchLiveMetadataOrNull() ?: run {
            logger.warn(
                "MaterialSymbols codegen: could not reach $METADATA_ENDPOINT_URL, " +
                    "falling back to the bundled icon metadata snapshot."
            )
            loadBundledFallback()
        }

        val metadata = json.decodeFromString(FontMetadata.serializer(), rawJson)

        val iconNames = metadata.icons
            .asSequence()
            .filter { icon -> MATERIAL_SYMBOLS_FAMILIES.none { it in icon.unsupportedFamilies } }
            .map { it.name }
            .distinct()
            .sorted()
            .toList()

        val constantNames = iconNames.map(::iconNameToConstantName)
        require(constantNames.size == constantNames.toSet().size) {
            "Duplicate MaterialSymbols constant names would be generated — the icon catalog's " +
                "naming shape must have changed; iconNameToConstantName needs a closer look."
        }

        buildFileSpec(iconNames).writeTo(outputDirectory.get().asFile)
    }

    private fun fetchLiveMetadataOrNull(): String? = try {
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build()
        val request = HttpRequest.newBuilder(URI.create(METADATA_ENDPOINT_URL))
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() == 200) stripXssiPrefix(response.body()) else null
    } catch (e: Exception) {
        null
    }

    private fun loadBundledFallback(): String {
        val resource = javaClass.classLoader.getResourceAsStream(FALLBACK_ICONS_RESOURCE_PATH)
            ?: error("Bundled fallback resource '$FALLBACK_ICONS_RESOURCE_PATH' is missing from the plugin jar")
        return resource.bufferedReader().use { it.readText() }
    }

    /** Google prefixes the response with a `)]}'` XSSI-protection line that isn't valid JSON. */
    private fun stripXssiPrefix(body: String): String = body.substringAfter('\n')

    private fun buildFileSpec(iconNames: List<String>): FileSpec {
        val objectBuilder = TypeSpec.objectBuilder(GENERATED_TYPE_NAME)

        iconNames.forEach { iconName ->
            objectBuilder.addProperty(
                PropertySpec.builder(iconNameToConstantName(iconName), STRING)
                    .addModifiers(KModifier.CONST)
                    .initializer("%S", iconName)
                    .build()
            )
        }

        return FileSpec.builder(GENERATED_PACKAGE, GENERATED_TYPE_NAME)
            .addFileComment("Generated by the material-symbols-codegen Gradle plugin. Do not edit by hand.")
            .addType(objectBuilder.build())
            .build()
    }
}
