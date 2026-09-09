package dev.catbit.material_symbols.sample.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.catbit.material_symbols.sample.ui.State

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CategoryFilterGrid(
    modifier: Modifier = Modifier,
    categories: List<State.Displaying.MaterialSymbolCategory>,
    selectedCategory: State.Displaying.MaterialSymbolCategory?,
    onCategoryClicked: (State.Displaying.MaterialSymbolCategory) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.chunked(2).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCategories.forEach { category ->
                    ToggleButton(
                        modifier = Modifier.weight(1f),
                        checked = category == selectedCategory,
                        onCheckedChange = { onCategoryClicked(category) },
                        colors = ToggleButtonDefaults.toggleButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        )
                    ) {
                        Text(
                            text = category.label,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }

                if (rowCategories.size < 2) {
                    Row(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}