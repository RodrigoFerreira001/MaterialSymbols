package dev.catbit.material_symbols.sample.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.roundToInt

@Composable
fun DiscreteSlider(
    modifier: Modifier = Modifier,
    title: String,
    values: List<Int>,
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    minLabel: String,
    maxLabel: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        val interactionSource = remember { MutableInteractionSource() }
        val currentIndex = values.indexOf(currentValue).coerceAtLeast(0)

        Slider(
            value = currentIndex.toFloat(),
            onValueChange = { newIndex ->
                onValueChange(values[newIndex.roundToInt().coerceIn(values.indices)])
            },
            valueRange = 0f..(values.size - 1).toFloat(),
            steps = values.size - 2,
            interactionSource = interactionSource,
            thumb = {
                SliderThumbWithLabel(
                    interactionSource = interactionSource,
                    label = currentValue.toString()
                )
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = minLabel,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = maxLabel,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}