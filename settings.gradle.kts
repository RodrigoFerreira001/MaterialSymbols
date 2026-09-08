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
include(":material-symbols-sample:shared")
include(":material-symbols-sample:androidApp")
include(":material-symbols-sample:desktopApp")
include(":material-symbols-sample:webApp")
