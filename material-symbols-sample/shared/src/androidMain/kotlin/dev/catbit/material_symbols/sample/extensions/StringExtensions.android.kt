package dev.catbit.material_symbols.sample.extensions

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

actual fun String.toClipEntry() = ClipEntry(ClipData.newPlainText(this, this))