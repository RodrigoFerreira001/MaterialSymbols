package dev.catbit.material_symbols.sample.ui.components

import androidx.compose.ui.graphics.Color

internal data class PaletteColor(
    val label: String,
    val color: Color
)

internal val ColorPickerPalette: List<PaletteColor> = listOf(
    PaletteColor("black", Color(0, 0, 0)),
    PaletteColor("dark gray 4", Color(67, 67, 67)),
    PaletteColor("dark gray 3", Color(102, 102, 102)),
    PaletteColor("dark gray 2", Color(153, 153, 153)),
    PaletteColor("dark gray 1", Color(183, 183, 183)),
    PaletteColor("gray", Color(204, 204, 204)),
    PaletteColor("light gray 1", Color(217, 217, 217)),
    PaletteColor("light gray 2", Color(239, 239, 239)),
    PaletteColor("light gray 3", Color(243, 243, 243)),
    PaletteColor("white", Color(255, 255, 255)),

    PaletteColor("red berry", Color(139, 26, 16)),
    PaletteColor("red", Color(234, 51, 35)),
    PaletteColor("orange", Color(241, 158, 57)),
    PaletteColor("yellow", Color(255, 255, 85)),
    PaletteColor("green", Color(117, 251, 76)),
    PaletteColor("cyan", Color(117, 251, 253)),
    PaletteColor("cornflower blue", Color(89, 133, 225)),
    PaletteColor("blue", Color(0, 0, 245)),
    PaletteColor("purple", Color(140, 26, 246)),
    PaletteColor("magenta", Color(234, 51, 247)),

    PaletteColor("light red berry 3", Color(223, 186, 177)),
    PaletteColor("light red 3", Color(238, 206, 205)),
    PaletteColor("light orange 3", Color(248, 230, 208)),
    PaletteColor("light yellow 3", Color(253, 243, 208)),
    PaletteColor("light green 3", Color(220, 234, 213)),
    PaletteColor("light cyan 3", Color(211, 224, 227)),
    PaletteColor("light cornflower blue 3", Color(204, 218, 245)),
    PaletteColor("light blue 3", Color(211, 226, 241)),
    PaletteColor("light purple 3", Color(216, 210, 231)),
    PaletteColor("light magenta 3", Color(230, 210, 220)),

    PaletteColor("light red berry 2", Color(208, 131, 112)),
    PaletteColor("light red 2", Color(223, 157, 155)),
    PaletteColor("light orange 2", Color(242, 205, 162)),
    PaletteColor("light yellow 2", Color(251, 230, 163)),
    PaletteColor("light green 2", Color(189, 214, 172)),
    PaletteColor("light cyan 2", Color(169, 195, 200)),
    PaletteColor("light cornflower blue 2", Color(170, 193, 240)),
    PaletteColor("light blue 2", Color(167, 196, 229)),
    PaletteColor("light purple 2", Color(178, 168, 211)),
    PaletteColor("light magenta 2", Color(206, 168, 188)),

    PaletteColor("light red berry 1", Color(189, 76, 49)),
    PaletteColor("light red 1", Color(209, 109, 106)),
    PaletteColor("light orange 1", Color(236, 181, 118)),
    PaletteColor("light yellow 1", Color(249, 219, 120)),
    PaletteColor("light green 1", Color(157, 195, 132)),
    PaletteColor("light cyan 1", Color(128, 164, 174)),
    PaletteColor("light cornflower blue 1", Color(120, 157, 229)),
    PaletteColor("light blue 1", Color(124, 167, 216)),
    PaletteColor("light purple 1", Color(139, 125, 190)),
    PaletteColor("light magenta 1", Color(184, 126, 159)),

    PaletteColor("dark red berry 1", Color(153, 43, 21)),
    PaletteColor("dark red 1", Color(187, 39, 26)),
    PaletteColor("dark orange 1", Color(218, 149, 75)),
    PaletteColor("dark yellow 1", Color(234, 196, 82)),
    PaletteColor("dark green 1", Color(120, 167, 90)),
    PaletteColor("dark cyan 1", Color(84, 128, 140)),
    PaletteColor("dark cornflower blue 1", Color(75, 119, 209)),
    PaletteColor("dark blue 1", Color(80, 132, 193)),
    PaletteColor("dark purple 1", Color(99, 79, 162)),
    PaletteColor("dark magenta 1", Color(155, 82, 120)),

    PaletteColor("dark red berry 2", Color(122, 41, 23)),
    PaletteColor("dark red 2", Color(140, 26, 16)),
    PaletteColor("dark orange 2", Color(169, 100, 36)),
    PaletteColor("dark yellow 2", Color(184, 146, 48)),
    PaletteColor("dark green 2", Color(72, 117, 44)),
    PaletteColor("dark cyan 2", Color(38, 78, 91)),
    PaletteColor("dark cornflower blue 2", Color(40, 84, 197)),
    PaletteColor("dark blue 2", Color(37, 82, 144)),
    PaletteColor("dark purple 2", Color(50, 29, 113)),
    PaletteColor("dark magenta 2", Color(107, 35, 70)),

    PaletteColor("dark red berry 3", Color(83, 22, 7)),
    PaletteColor("dark red 3", Color(93, 14, 7)),
    PaletteColor("dark orange 3", Color(113, 66, 22)),
    PaletteColor("dark yellow 3", Color(122, 97, 29)),
    PaletteColor("dark green 3", Color(49, 77, 28)),
    PaletteColor("dark cyan 3", Color(24, 51, 60)),
    PaletteColor("dark cornflower blue 3", Color(39, 68, 131)),
    PaletteColor("dark blue 3", Color(23, 54, 96)),
    PaletteColor("dark purple 3", Color(30, 18, 74)),
    PaletteColor("dark magenta 3", Color(70, 21, 47))
)

internal const val ColorPickerGridColumns = 10
