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

rootProject.name = "MaterialSymbols"
include(":material-symbols")
