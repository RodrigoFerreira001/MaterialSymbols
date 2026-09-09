package dev.catbit.material_symbols.sample.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.catbit.material_symbols.MaterialSymbol
import dev.catbit.material_symbols.MaterialSymbolFontsConfig
import dev.catbit.material_symbols.MaterialSymbols
import dev.catbit.material_symbols.MaterialSymbolsRenderingScope
import dev.catbit.material_symbols.sample.extensions.toClipEntry
import dev.catbit.material_symbols.sample.extensions.toColorOrNull
import dev.catbit.material_symbols.sample.extensions.withNotNull
import dev.catbit.material_symbols.sample.ui.components.CategoryFilterGrid
import dev.catbit.material_symbols.sample.ui.components.ColorPicker
import dev.catbit.material_symbols.sample.ui.components.DiscreteSlider
import dev.catbit.material_symbols.sample.ui.components.MaterialSymbolCard
import dev.catbit.material_symbols.sample.ui.components.SearchBar
import dev.catbit.material_symbols.sample.ui.components.StyleDropdown
import dev.catbit.material_symbols.sample.ui.theme.darkScheme
import dev.catbit.material_symbols.sample.ui.theme.lightScheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    darkMode: Boolean = isSystemInDarkTheme()
) {

    val viewModel = viewModel { AppStateHolder(darkMode) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MaterialTheme(
        colorScheme = if (uiState.darkMode) darkScheme else lightScheme
    ) {
        MaterialSymbolsRenderingScope {

            when (val state = uiState) {
                is State.Displaying -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        MainContent(
                            uiState = state,
                            onEvent = { viewModel.onEvent(it) }
                        )

                        AnimatedVisibility(
                            modifier = Modifier.align(Alignment.TopStart),
                            visible = state.displayFilter,
                            enter = slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth }
                            ),
                            exit = slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                        ) {
                            Filters(
                                uiState = state,
                                onEvent = { viewModel.onEvent(it) }
                            )
                        }

                        AnimatedVisibility(
                            modifier = Modifier.align(Alignment.TopEnd),
                            visible = state.displayPreview,
                            enter = slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth }
                            ),
                            exit = slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth }
                            ),
                        ) {
                            Preview(
                                uiState = state,
                                onEvent = { viewModel.onEvent(it) }
                            )
                        }
                    }
                }

                is State.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    uiState: State.Displaying,
    onEvent: (Event) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = "MaterialSymbols",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )

                SearchBar(
                    modifier = Modifier.weight(1f),
                    query = uiState.searchQuery,
                    placeholder = {
                        Text("Search for a symbol")
                    },
                    onQueryChange = {
                        onEvent(Event.OnSearchQueryChanged(it))
                    },
                    leadingIcon = {
                        MaterialSymbol(
                            iconName = MaterialSymbols.SEARCH,
                            contentDescription = "Search icon",
                            size = 24.dp
                        )
                    }
                )

                IconButton(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = {
                        onEvent(Event.OnToggleDarkMode)
                    }
                ) {
                    MaterialSymbol(
                        iconName = if (uiState.darkMode) MaterialSymbols.LIGHT_MODE else MaterialSymbols.DARK_MODE,
                        contentDescription = "Light/Dark mode switch"
                    )
                }
            }

            OutlinedButton(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 24.dp),
                onClick = {
                    onEvent(Event.OnDisplayFiltersClicked)
                }
            ) {
                MaterialSymbol(
                    modifier = Modifier.padding(end = 8.dp),
                    iconName = MaterialSymbols.TUNE,
                    size = 24.dp,
                    contentDescription = "Search icon",
                )
                Text("Filters")
            }

            MaterialSymbolsRenderingScope(
                config = MaterialSymbolFontsConfig(
                    weight = FontWeight(uiState.filters.weight),
                    grade = uiState.filters.grade,
                    opticalSize = uiState.filters.opticalSize.sp
                )
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 112.dp),
                    contentPadding = PaddingValues(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val symbolsByCategory = uiState
                        .symbols
                        .filter { symbol ->
                            symbol.tags.any { tag ->
                                tag.contains(uiState.searchQuery)
                            }
                        }
                        .groupBy { it.category }
                        .filter { entry -> uiState.filters.selectedCategory?.let { entry.key == it.label } ?: true }

                    symbolsByCategory.forEach { (category, symbols) ->
                        item(
                            key = "category_$category",
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    top = 16.dp,
                                    bottom = 8.dp
                                )
                            )
                        }

                        items(
                            items = symbols,
                            key = { it.name }
                        ) { symbol ->
                            MaterialSymbolCard(
                                icon = symbol,
                                style = uiState.filters.style,
                                filled = uiState.filters.filled,
                                selected = uiState.preview?.symbol == symbol.name,
                                onClick = {
                                    onEvent(Event.OnSymbolClicked(symbol.name))
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
private fun Filters(
    uiState: State.Displaying,
    onEvent: (Event) -> Unit
) {
    val shape = RoundedCornerShape(
        topEnd = 16.dp,
        bottomEnd = 16.dp
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(400.dp)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                clip = false
            )
            .verticalScroll(rememberScrollState())
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    end = 8.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.End
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = {
                    onEvent(Event.OnResetAllFilters)
                }
            ) {
                MaterialSymbol(
                    modifier = Modifier.padding(end = 8.dp),
                    iconName = MaterialSymbols.REFRESH,
                    size = 24.dp,
                    contentDescription = "Reset filters",
                )
                Text("Reset all")
            }

            IconButton(
                onClick = {
                    onEvent(Event.OnHideFiltersClicked)
                }
            ) {
                MaterialSymbol(
                    iconName = MaterialSymbols.CLOSE,
                    contentDescription = "Close icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fill",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = uiState.filters.filled,
                    onCheckedChange = {
                        onEvent(Event.OnFillClicked)
                    }
                )
            }

            DiscreteSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                title = "Weight",
                values = uiState.materialSymbolWeights,
                currentValue = uiState.filters.weight,
                onValueChange = { onEvent(Event.OnWeightChanged(it)) },
                minLabel = "100",
                maxLabel = "700"
            )

            DiscreteSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                title = "Grade",
                values = uiState.materialSymbolGrades,
                currentValue = uiState.filters.grade,
                onValueChange = { onEvent(Event.OnGrandeChanged(it)) },
                minLabel = "-25 (low)",
                maxLabel = "200 (high emphasis)"
            )

            DiscreteSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                title = "Optical size",
                values = uiState.materialSymbolOpticalSizes,
                currentValue = uiState.filters.opticalSize,
                onValueChange = { onEvent(Event.OnOpticalSizeChanged(it)) },
                minLabel = "20px",
                maxLabel = "48px"
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Style",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            StyleDropdown(
                modifier = Modifier.fillMaxWidth(),
                selectedStyle = uiState.filters.style,
                onStyleSelected = { onEvent(Event.OnStyleChanged(it)) }
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Category",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            CategoryFilterGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                categories = State.Displaying.MaterialSymbolCategory.entries,
                selectedCategory = uiState.filters.selectedCategory,
                onCategoryClicked = { onEvent(Event.OnCategoryClicked(it)) }
            )
        }
    }
}

@Composable
private fun Preview(
    uiState: State.Displaying,
    onEvent: (Event) -> Unit
) {
    withNotNull(uiState.preview) {
        val shape = RoundedCornerShape(
            topEnd = 16.dp,
            bottomEnd = 16.dp
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(400.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = shape,
                    clip = false
                )
                .verticalScroll(rememberScrollState())
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        end = 8.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.End
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onEvent(Event.OnHidePreviewClicked)
                    }
                ) {
                    MaterialSymbol(
                        iconName = MaterialSymbols.CLOSE,
                        contentDescription = "Close icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.symbols.first { it.name == symbol }.label,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(
                        onClick = {
                            onEvent(Event.OnResetPreview)
                        }
                    ) {
                        MaterialSymbol(
                            iconName = MaterialSymbols.REFRESH,
                            contentDescription = "Reset preview",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.extraLarge
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        MaterialSymbolsRenderingScope(
                            config = MaterialSymbolFontsConfig(
                                weight = FontWeight(uiState.filters.weight),
                                grade = uiState.filters.grade,
                                opticalSize = uiState.filters.opticalSize.sp
                            )
                        ) {
                            MaterialSymbol(
                                iconName = symbol,
                                contentDescription = "Material symbol preview",
                                size = size.dp,
                                tint = colorHex.toColorOrNull()
                            )
                        }

                        val clipboard = LocalClipboard.current
                        val coroutineScope = rememberCoroutineScope()

                        IconButton(
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .align(Alignment.BottomEnd),
                            onClick = {
                                coroutineScope.launch {
                                    clipboard.setClipEntry(symbol.toClipEntry())
                                }
                            }
                        ) {
                            MaterialSymbol(
                                iconName = MaterialSymbols.CONTENT_COPY,
                                contentDescription = "Copy the symbol name to clipboard"
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Size",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    onEvent(Event.OnDecreasePreviewSize)
                                }
                            ) {
                                MaterialSymbol(
                                    iconName = MaterialSymbols.REMOVE,
                                    contentDescription = "Decrease icon size",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Text(
                                text = size.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            IconButton(
                                onClick = {
                                    onEvent(Event.OnIncreasePreviewSize)
                                }
                            ) {
                                MaterialSymbol(
                                    iconName = MaterialSymbols.ADD,
                                    contentDescription = "Increase icon size",
                                    tint = MaterialTheme.colorScheme.onSurface

                                )
                            }
                        }

                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = "Color",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        SplitButtonLayout(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            leadingButton = {
                                var colorPickerExpanded by remember { mutableStateOf(false) }

                                Box {
                                    SplitButtonDefaults.LeadingButton(
                                        onClick = {
                                            colorPickerExpanded = true
                                        }
                                    ) {
                                        MaterialSymbol(
                                            iconName = MaterialSymbols.PALETTE,
                                            contentDescription = "Pick color"
                                        )
                                    }

                                    ColorPicker(
                                        expanded = colorPickerExpanded,
                                        onDismissRequest = { colorPickerExpanded = false },
                                        selectedColorHex = colorHex,
                                        onColorSelected = {
                                            onEvent(Event.OnPreviewColorChanged(it))
                                            colorPickerExpanded = false
                                        }
                                    )
                                }
                            },
                            trailingButton = {
                                val isValidColor = colorHex.toColorOrNull() != null

                                SplitButtonDefaults.TrailingButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    checked = false,
                                    onCheckedChange = {

                                    }
                                ) {
                                    BasicTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = colorHex,
                                        onValueChange = {
                                            onEvent(Event.OnPreviewColorChanged(it))
                                        },
                                        singleLine = true,
                                        textStyle = MaterialTheme.typography.labelLarge.copy(
                                            color = if (isValidColor) {
                                                LocalContentColor.current
                                            } else {
                                                MaterialTheme.colorScheme.error
                                            }
                                        ),
                                        cursorBrush = SolidColor(LocalContentColor.current)
                                    )
                                }
                            }
                        )
                    }
                }

                SymbolTags(
                    tags = uiState.symbols.first { it.name == symbol }.tags
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.SymbolTags(
    tags: List<String>
) {
    var expanded by remember(tags) { mutableStateOf(false) }
    var canOverflow by remember(tags) { mutableStateOf(false) }

    Text(
        text = tags.joinToString(", "),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = if (expanded) Int.MAX_VALUE else 2,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { layoutResult ->
            if (!expanded) {
                canOverflow = layoutResult.hasVisualOverflow
            }
        }
    )

    if (canOverflow) {
        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                expanded = !expanded
            }
        ) {
            Text(if (expanded) "Show less" else "Show more")
        }
    }
}
