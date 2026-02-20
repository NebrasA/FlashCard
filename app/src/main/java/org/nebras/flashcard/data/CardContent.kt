package org.nebras.flashcard.data

sealed interface CardContent {
    data class Text(val value: String) : CardContent
    data class FretboardDiagram(
        val textLabel: String,
        val dots: List<FretboardDot>,
        val startFret: Int = 1,
        val fretCount: Int = 5
    ) : CardContent
}

data class FretboardDot(
    val string: Int,   // 1=high E, 2=B, 3=G, 4=D, 5=A, 6=low E
    val fret: Int,     // absolute fret position
    val type: DotType,
    val label: String = ""
)

enum class DotType { ROOT, INTERVAL }
