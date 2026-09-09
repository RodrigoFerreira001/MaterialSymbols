package dev.catbit.material_symbols.sample.extensions

import androidx.compose.ui.platform.ClipEntry

actual fun String.toClipEntry() = ClipEntry.withPlainText(this)