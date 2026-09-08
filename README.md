<p align="center"><b>Google's Material Symbols, as Compose Multiplatform</b></p>
<p align="center">A single <code>MaterialSymbol</code> composable over the 3 official variable icon fonts (Outlined, Rounded, Sharp), plus a build-time-generated, typo-proof catalog of every valid icon name.</p>

<p align="center">
  <a href="https://search.maven.org/search?q=g:dev.catbit+a:material-symbols"><img src="https://img.shields.io/maven-central/v/dev.catbit/material-symbols?label=Maven%20Central&color=2A6DB2" alt="Maven Central"></a>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Kotlin Multiplatform" src="https://img.shields.io/badge/Kotlin%20Multiplatform-Android%20|%20iOS%20|%20JVM%20|%20JS%20|%20Wasm-7F52FF?logo=kotlin&logoColor=white">
  <img alt="Compose Multiplatform" src="https://img.shields.io/badge/Compose%20Multiplatform-UI%20layer-4285F4?logo=jetpackcompose&logoColor=white">
  <img alt="License" src="https://img.shields.io/badge/license-Apache%202.0-informational">
</p>

---

Google's [Material Symbols](https://fonts.google.com/icons) ship as variable fonts, not as a fixed set of vector drawables — each glyph's fill, weight, grade and optical size are font axes, not separate assets. This library wraps those 3 fonts (Outlined, Rounded, Sharp) as a single Compose Multiplatform composable, and generates a `MaterialSymbols` constants catalog straight from Google's own icon metadata at build time, so an icon name is always valid or the build won't compile it.

## Usage

```kotlin
MaterialSymbolsRenderingScope {
    MaterialSymbol(
        iconName = MaterialSymbols.SETTINGS,
        contentDescription = "Settings",
        style = MaterialSymbolStyle.ROUNDED,
        filled = true
    )
}
```

- `MaterialSymbolsRenderingScope` loads the 3 icon fonts once (configurable via `MaterialSymbolFontsConfig` — weight, grade, optical size) and provides them to every `MaterialSymbol` composed underneath it. Wrap the root of your UI tree in it once.
- `MaterialSymbols` is generated from Google's live icon catalog — `MaterialSymbols.SETTINGS`, `MaterialSymbols.HOME`, and so on for every icon Material Symbols publishes, always in sync with what the 3 bundled fonts actually support.

## Install

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

```kotlin
// build.gradle.kts
dependencies {
    implementation("dev.catbit:material-symbols:1.0.0")
}
```

## License

Apache License 2.0 — see [LICENSE](LICENSE).
