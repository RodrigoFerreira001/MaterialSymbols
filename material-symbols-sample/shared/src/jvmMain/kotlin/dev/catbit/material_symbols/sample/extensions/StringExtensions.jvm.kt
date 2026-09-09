package dev.catbit.material_symbols.sample.extensions

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual fun String.toClipEntry() = ClipEntry(java.awt.datatransfer.StringSelection(this))