package dev.catbit.material_symbols.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.catbit.material_symbols.MaterialSymbol
import dev.catbit.material_symbols.MaterialSymbolStyle
import dev.catbit.material_symbols.MaterialSymbols
import dev.catbit.material_symbols.MaterialSymbolsRenderingScope

/** A handful of icons from every corner of the catalog, just to show off style/fill variance. */
private val showcaseIcons = listOf(
    MaterialSymbols.HOME,
    MaterialSymbols.SEARCH,
    MaterialSymbols.SETTINGS,
    MaterialSymbols.FAVORITE,
    MaterialSymbols.NOTIFICATIONS,
    MaterialSymbols.STAR,
    MaterialSymbols.ARROW_BACK,
    MaterialSymbols.DELETE,
    MaterialSymbols.EDIT,
    MaterialSymbols.SHARE,
    MaterialSymbols.CHECK_CIRCLE,
    MaterialSymbols.PERSON
)

@Composable
fun App() {
    MaterialTheme {
        var style by remember { mutableStateOf(MaterialSymbolStyle.OUTLINED) }
        var filled by remember { mutableStateOf(false) }

        MaterialSymbolsRenderingScope {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Material Symbols") }) }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    StyleControls(
                        style = style,
                        onStyleChange = { style = it },
                        filled = filled,
                        onFilledChange = { filled = it }
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 96.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(showcaseIcons) { iconName ->
                            IconCard(iconName = iconName, style = style, filled = filled)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StyleControls(
    style: MaterialSymbolStyle,
    onStyleChange: (MaterialSymbolStyle) -> Unit,
    filled: Boolean,
    onFilledChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        SingleChoiceSegmentedButtonRow {
            MaterialSymbolStyle.entries.forEachIndexed { index, entry ->
                SegmentedButton(
                    selected = style == entry,
                    onClick = { onStyleChange(entry) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = MaterialSymbolStyle.entries.size
                    )
                ) {
                    Text(entry.name)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Filled")
            Switch(checked = filled, onCheckedChange = onFilledChange)
        }
    }
}

@Composable
private fun IconCard(iconName: String, style: MaterialSymbolStyle, filled: Boolean) {
    Card {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MaterialSymbol(
                iconName = iconName,
                contentDescription = null,
                style = style,
                filled = filled,
                size = 32.dp
            )
        }
    }
}
