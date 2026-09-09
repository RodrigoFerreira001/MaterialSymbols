package dev.catbit.material_symbols.sample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import dev.catbit.material_symbols.sample.ui.App

@OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)
fun main() {
    ComposeViewport {
        App()
    }
}
