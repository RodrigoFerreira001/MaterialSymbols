package dev.catbit.material_symbols.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.catbit.material_symbols.sample.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Material Symbols"
    ) {
        App()
    }
}
