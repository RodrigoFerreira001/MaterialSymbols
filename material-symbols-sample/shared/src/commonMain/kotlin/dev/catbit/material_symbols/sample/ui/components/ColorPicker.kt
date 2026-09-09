package dev.catbit.material_symbols.sample.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import dev.catbit.material_symbols.MaterialSymbol
import dev.catbit.material_symbols.MaterialSymbols
import dev.catbit.material_symbols.sample.extensions.isPerceivedDark
import dev.catbit.material_symbols.sample.extensions.toColorOrNull
import dev.catbit.material_symbols.sample.extensions.toHexString

@Composable
fun ColorPicker(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedColorHex: String,
    onColorSelected: (String) -> Unit
) {
    if (!expanded) return

    val selectedColor = selectedColorHex.toColorOrNull()
    val density = LocalDensity.current

    Popup(
        popupPositionProvider = remember(density) { AbovePopupPositionProvider(spacing = 8.dp, density = density) },
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 3.dp,
            shadowElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .width(280.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Color Picker",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ColorPickerPalette.chunked(ColorPickerGridColumns).forEach { rowColors ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        rowColors.forEach { paletteColor ->
                            ColorSwatch(
                                color = paletteColor.color,
                                contentDescription = paletteColor.label,
                                selected = selectedColor == paletteColor.color,
                                onClick = {
                                    onColorSelected(paletteColor.color.toHexString())
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    color: Color,
    contentDescription: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val isDark = color.isPerceivedDark
    val contentColor = if (isDark) Color.White else Color.Black

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
            .then(
                if (isDark) {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            MaterialSymbol(
                iconName = MaterialSymbols.CHECK,
                contentDescription = "$contentDescription (selected)",
                size = 16.dp,
                tint = contentColor
            )
        }
    }
}

private class AbovePopupPositionProvider(
    private val spacing: Dp,
    private val density: Density
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val spacingPx = with(density) { spacing.roundToPx() }
        return IntOffset(
            x = anchorBounds.left,
            y = anchorBounds.top - popupContentSize.height - spacingPx
        )
    }
}
