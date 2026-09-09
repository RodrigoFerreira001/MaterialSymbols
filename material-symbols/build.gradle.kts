@file:OptIn(ExperimentalWasmDsl::class)

import dev.catbit.material_symbols.codegen.GenerateMaterialSymbolsTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    id("dev.catbit.material-symbols.codegen")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "dev.catbit",
        artifactId = "material-symbols",
        version = libs.versions.material.symbols.get()
    )

    pom {
        name = "Material Symbols"
        description = "A Kotlin Multiplatform Compose wrapper library for Google's Material Symbols variable icon fonts."
        url = "https://github.com/RodrigoFerreira001/MaterialSymbols"

        licenses {
            license {
                name = "Apache License 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }

        developers {
            developer {
                id = "RodrigoFerreira001"
                name = "Rodrigo Ferreira"
                url = "https://github.com/RodrigoFerreira001"
            }
        }

        scm {
            url = "https://github.com/RodrigoFerreira001/MaterialSymbols"
            connection = "scm:git:git://github.com/RodrigoFerreira001/MaterialSymbols.git"
            developerConnection = "scm:git:ssh://git@github.com/RodrigoFerreira001/MaterialSymbols.git"
        }
    }
}

kotlin {
    jvm {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    android {
        namespace = "dev.catbit.material_symbols"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }

        androidResources {
            enable = true
        }
    }
    iosArm64()
    iosSimulatorArm64()
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            // Compose
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.components.ui.tooling.preview)
            implementation(libs.compose.ui.tooling.preview)
        }

        // Only the Android preview renderer needs `ui-tooling` (ComposeViewAdapter) on its
        // classpath — kept off commonMain so it doesn't ship to iOS/JS/Wasm/JVM consumers.
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
        }

        val generateMaterialSymbols = tasks.named<GenerateMaterialSymbolsTask>("generateMaterialSymbols")
        commonMain {
            kotlin.srcDir(generateMaterialSymbols.map { it.outputDirectory })
        }
    }
}

compose {
    resources {
        publicResClass = true
        packageOfResClass = "dev.catbit.material_symbols.resources"
        generateResClass = always
    }
}
