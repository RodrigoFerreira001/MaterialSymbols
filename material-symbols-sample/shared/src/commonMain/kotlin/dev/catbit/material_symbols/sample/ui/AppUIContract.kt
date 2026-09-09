package dev.catbit.material_symbols.sample.ui

import dev.catbit.material_symbols.MaterialSymbolStyle
import dev.catbit.material_symbols.sample.data.MaterialSymbolInstance

sealed interface State {

    val darkMode: Boolean

    data class Loading(
        override val darkMode: Boolean
    ) : State

    data class Displaying(
        override val darkMode: Boolean,
        val symbols: List<MaterialSymbolInstance>,
        val searchQuery: String = "",
        val filters: Filters = Filters(),
        val preview: Preview? = null,
        val displayFilter: Boolean = false,
        val displayPreview: Boolean = false,
    ) : State {

        data class Filters(
            val filled: Boolean = false,
            val weight: Int = 400,
            val grade: Int = 0,
            val opticalSize: Int = 24,
            val style: MaterialSymbolStyle = MaterialSymbolStyle.OUTLINED,
            val selectedCategory: MaterialSymbolCategory? = null
        )

        data class Preview(
            val symbol: String,
            val size: Int = 24,
            val colorHex: String
        )

        val materialSymbolWeights = listOf(100, 200, 300, 400, 500, 600, 700)
        val materialSymbolGrades = listOf(-25, 0, 200)
        val materialSymbolOpticalSizes = listOf(20, 24, 40, 48)

        enum class MaterialSymbolCategory(val label: String) {
            Actions("Actions"),
            Activities("Activities"),
            Android("Android"),
            AudioVideo("Audio&Video"),
            Business("Business"),
            Communicate("Communicate"),
            Hardware("Hardware"),
            Home("Home"),
            Household("Household"),
            Images("Images"),
            Maps("Maps"),
            Privacy("Privacy"),
            Social("Social"),
            Text("Text"),
            Transit("Transit"),
            Travel("Travel"),
            UIActions("UI actions")
        }
    }
}

sealed interface Event {
    data object OnDisplayFiltersClicked : Event
    data object OnHideFiltersClicked : Event
    data class OnSymbolClicked(val symbol: String): Event
    data class OnSearchQueryChanged(val newQuery: String): Event

    data object OnHidePreviewClicked : Event
    data object OnPreviewClosed: Event
    data object OnToggleDarkMode : Event
    data object OnFillClicked : Event
    data object OnResetAllFilters : Event
    data object OnResetPreview : Event
    data object OnIncreasePreviewSize : Event
    data object OnDecreasePreviewSize : Event

    data class OnWeightChanged(val weight: Int) : Event
    data class OnGrandeChanged(val grade: Int) : Event
    data class OnOpticalSizeChanged(val opticalSize: Int) : Event
    data class OnStyleChanged(val style: MaterialSymbolStyle) : Event
    data class OnCategoryClicked(val category: State.Displaying.MaterialSymbolCategory) : Event
    data class OnPreviewColorChanged(val colorHex: String) : Event
}