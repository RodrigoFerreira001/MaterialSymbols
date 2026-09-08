package dev.catbit.material_symbols.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Material Symbols"
    ) {
        App()
    }
}
