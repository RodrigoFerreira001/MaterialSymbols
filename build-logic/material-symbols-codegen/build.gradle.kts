plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
}

group = "dev.catbit.material_symbols.codegen"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

gradlePlugin {
    plugins {
        register("materialSymbolsCodegen") {
            id = "dev.catbit.material-symbols.codegen"
            implementationClass = "dev.catbit.material_symbols.codegen.MaterialSymbolsCodegenPlugin"
        }
    }
}
