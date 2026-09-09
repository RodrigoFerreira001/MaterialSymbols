package dev.catbit.material_symbols.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.catbit.material_symbols.MaterialSymbolStyle
import dev.catbit.material_symbols.sample.data.ShowcaseIcons
import dev.catbit.material_symbols.sample.extensions.toHexString
import dev.catbit.material_symbols.sample.extensions.updateAs
import dev.catbit.material_symbols.sample.ui.theme.darkScheme
import dev.catbit.material_symbols.sample.ui.theme.lightScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppStateHolder(
    darkMode: Boolean
) : ViewModel() {

    private val internalUIState = MutableStateFlow<State>(State.Loading(darkMode))
    val uiState get() = internalUIState.asStateFlow()

    init {
        viewModelScope.launch {
            val symbols = ShowcaseIcons.load()
            internalUIState.value = State.Displaying(
                symbols = symbols,
                darkMode = darkMode
            )
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnSearchQueryChanged -> onSearchQueryChanged(event.newQuery)
            Event.OnDisplayFiltersClicked -> onFilterClicked()
            is Event.OnSymbolClicked -> onSymbolClicked(event.symbol)
            Event.OnHideFiltersClicked -> onHideFiltersClicked()
            Event.OnHidePreviewClicked -> onHidePreviewClicked()
            Event.OnPreviewClosed -> onPreviewClosed()
            Event.OnToggleDarkMode -> onToggleDarkMode()
            is Event.OnWeightChanged -> onWeightChanged(event.weight)
            Event.OnFillClicked -> onFillClicked()
            is Event.OnGrandeChanged -> onGradeChanged(event.grade)
            is Event.OnOpticalSizeChanged -> onOpticalSizeChanged(event.opticalSize)
            is Event.OnStyleChanged -> onStyleChanged(event.style)
            is Event.OnCategoryClicked -> onCategoryClicked(event.category)
            Event.OnResetAllFilters -> onResetAllFilters()
            Event.OnResetPreview -> onResetPreview()
            Event.OnIncreasePreviewSize -> onIncreasePreviewSize()
            Event.OnDecreasePreviewSize -> onDecreasePreviewSize()
            is Event.OnPreviewColorChanged -> onPreviewColorChanged(event.colorHex)
        }
    }

    private fun onResetPreview() {
        internalUIState.updateAs<State.Displaying> {
            copy(
                preview = preview?.let {
                    State.Displaying.Preview(
                        symbol = it.symbol,
                        colorHex = (if (darkMode) darkScheme.onSurface else lightScheme.onSurface).toHexString()
                    )
                }
            )
        }
    }

    private fun onIncreasePreviewSize() {
        internalUIState.updateAs<State.Displaying> {
            copy(
                preview = preview?.copy(
                    size = (preview.size + PreviewSizeStep).coerceIn(
                        MinPreviewSize,
                        MaxPreviewSize
                    )
                )
            )
        }
    }

    private fun onDecreasePreviewSize() {
        internalUIState.updateAs<State.Displaying> {
            copy(
                preview = preview?.copy(
                    size = (preview.size - PreviewSizeStep).coerceIn(
                        MinPreviewSize,
                        MaxPreviewSize
                    )
                )
            )
        }
    }

    private fun onPreviewColorChanged(colorHex: String) {
        internalUIState.updateAs<State.Displaying> {
            copy(preview = preview?.copy(colorHex = colorHex))
        }
    }

    private fun onResetAllFilters() {
        internalUIState.updateAs<State.Displaying> {
            copy(
                filters = State.Displaying.Filters()
            )
        }
    }

    private companion object {
        const val MinPreviewSize = 16
        const val MaxPreviewSize = 80
        const val PreviewSizeStep = 4
    }

    private fun onSearchQueryChanged(newQuery: String) {
        internalUIState.updateAs<State.Displaying> {
            copy(
                searchQuery = newQuery
            )
        }
    }

    private fun onFilterClicked() {
        internalUIState.updateAs<State.Displaying> {
            copy(
                displayFilter = true,
                displayPreview = false
            )
        }
    }

    private fun onSymbolClicked(symbol: String) {
        internalUIState.updateAs<State.Displaying> {
            copy(
                preview = State.Displaying.Preview(
                    symbol = symbol,
                    colorHex = (if (darkMode) darkScheme.onSurface else lightScheme.onSurface).toHexString()
                ),
                displayPreview = true,
                displayFilter = false
            )
        }
    }

    private fun onHideFiltersClicked() {
        internalUIState.updateAs<State.Displaying> {
            copy(displayFilter = false)
        }
    }

    private fun onHidePreviewClicked() {
        internalUIState.updateAs<State.Displaying> {
            copy(displayPreview = false)
        }
    }

    private fun onPreviewClosed() {
        internalUIState.updateAs<State.Displaying> {
            copy(preview = null)
        }
    }

    private fun onToggleDarkMode() {
        internalUIState.updateAs<State.Displaying> {
            copy(darkMode = !darkMode)
        }
    }

    private fun onWeightChanged(weight: Int) {
        internalUIState.updateAs<State.Displaying> {
            copy(filters = filters.copy(weight = weight))
        }
    }

    private fun onFillClicked() {
        internalUIState.updateAs<State.Displaying> {
            copy(filters = filters.copy(filled = !filters.filled))
        }
    }

    private fun onGradeChanged(grade: Int) {
        internalUIState.updateAs<State.Displaying> {
            copy(filters = filters.copy(grade = grade))
        }
    }

    private fun onOpticalSizeChanged(opticalSize: Int) {
        internalUIState.updateAs<State.Displaying> {
            copy(filters = filters.copy(opticalSize = opticalSize))
        }
    }

    private fun onStyleChanged(style: MaterialSymbolStyle) {
        internalUIState.updateAs<State.Displaying> {
            copy(filters = filters.copy(style = style))
        }
    }

    private fun onCategoryClicked(category: State.Displaying.MaterialSymbolCategory) {
        internalUIState.updateAs<State.Displaying> {
            copy(
                filters = filters.copy(
                    selectedCategory = if (filters.selectedCategory == category) null else category
                )
            )
        }
    }
}