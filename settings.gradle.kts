rootProject.name = "MaterialSymbolsWorkspace"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic/material-symbols-codegen")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":material-symbols")

// Sample
include(":sample:shared")
include(":sample:androidApp")
include(":sample:desktopApp")
include(":sample:webApp")
