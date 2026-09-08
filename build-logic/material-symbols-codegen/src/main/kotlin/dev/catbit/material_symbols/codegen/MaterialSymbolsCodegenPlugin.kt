package dev.catbit.material_symbols.codegen

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

/** Registers [GenerateMaterialSymbolsTask] as `generateMaterialSymbols`, writing into
 * `build/generated/material-symbols`. Applying projects are expected to wire that output directory
 * into a Kotlin source set themselves (see `library/build.gradle.kts`). */
class MaterialSymbolsCodegenPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.register<GenerateMaterialSymbolsTask>("generateMaterialSymbols") {
            outputDirectory.set(project.layout.buildDirectory.dir("generated/material-symbols"))
            group = "material symbols"
            description = "Generates the MaterialSymbols enum from Google's Material Symbols icon catalog."
        }
    }
}
