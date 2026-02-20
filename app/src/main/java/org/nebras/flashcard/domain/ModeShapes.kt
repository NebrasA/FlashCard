package org.nebras.flashcard.domain

import org.nebras.flashcard.data.Deck

// 18 sequential degrees starting from the mode's starting degree.
// "R" replaces degree 1. The shape (fret spacing) distinguishes modes.
private fun degreeLabels(rootDegree: Int): List<String> =
    (0 until 18).map { i ->
        val degree = (rootDegree - 1 + i) % 7 + 1
        if (degree == 1) "R" else degree.toString()
    }

private fun modeDots(
    rootDegree: Int,
    lowE: List<Int>, a: List<Int>, d: List<Int>,
    g: List<Int>, b: List<Int>, highE: List<Int>
): List<ModeDot> {
    val labels = degreeLabels(rootDegree)
    val strings = listOf(6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 2, 2, 2, 1, 1, 1)
    val offsets = lowE + a + d + g + b + highE
    return offsets.indices.map { i ->
        ModeDot(
            string = strings[i],
            relativeOffset = offsets[i],
            degreeLabel = labels[i]
        )
    }
}

private val ionian = ModeShape(
    name = "Ionian",
    rootDegree = 1,
    dots = modeDots(rootDegree = 1,
        lowE = listOf(0, 2, 4),  a = listOf(0, 2, 4),
        d = listOf(1, 2, 4),     g = listOf(1, 2, 4),
        b = listOf(2, 4, 5),     highE = listOf(2, 4, 5)
    )
)

private val dorian = ModeShape(
    name = "Dorian",
    rootDegree = 2,
    dots = modeDots(rootDegree = 2,
        lowE = listOf(0, 2, 3),  a = listOf(0, 2, 4),
        d = listOf(0, 2, 4),     g = listOf(0, 2, 4),
        b = listOf(2, 3, 5),     highE = listOf(2, 3, 5)
    )
)

private val phrygian = ModeShape(
    name = "Phrygian",
    rootDegree = 3,
    dots = modeDots(rootDegree = 3,
        lowE = listOf(0, 1, 3),  a = listOf(0, 2, 3),
        d = listOf(0, 2, 3),     g = listOf(0, 2, 4),
        b = listOf(1, 3, 5),     highE = listOf(1, 3, 5)
    )
)

private val lydian = ModeShape(
    name = "Lydian",
    rootDegree = 4,
    dots = modeDots(rootDegree = 4,
        lowE = listOf(0, 2, 4),  a = listOf(1, 2, 4),
        d = listOf(1, 2, 4),     g = listOf(1, 3, 4),
        b = listOf(2, 4, 5),     highE = listOf(2, 4, 6)
    )
)

private val mixolydian = ModeShape(
    name = "Mixolydian",
    rootDegree = 5,
    dots = modeDots(rootDegree = 5,
        lowE = listOf(0, 2, 4),  a = listOf(0, 2, 4),
        d = listOf(0, 2, 4),     g = listOf(1, 2, 4),
        b = listOf(2, 3, 5),     highE = listOf(2, 4, 5)
    )
)

private val aeolian = ModeShape(
    name = "Aeolian",
    rootDegree = 6,
    dots = modeDots(rootDegree = 6,
        lowE = listOf(0, 2, 3),  a = listOf(0, 2, 3),
        d = listOf(0, 2, 4),     g = listOf(0, 2, 4),
        b = listOf(1, 3, 5),     highE = listOf(2, 3, 5)
    )
)

private val locrian = ModeShape(
    name = "Locrian",
    rootDegree = 7,
    dots = modeDots(rootDegree = 7,
        lowE = listOf(0, 1, 3),  a = listOf(0, 1, 3),
        d = listOf(0, 2, 3),     g = listOf(0, 2, 3),
        b = listOf(1, 3, 5),     highE = listOf(1, 3, 5)
    )
)

private val allModeShapes = listOf(
    ionian, dorian, phrygian, lydian, mixolydian, aeolian, locrian
)

fun modeShapesDeck(): Deck = Deck(
    title = "Mode Shapes (3NPS)",
    cards = allModeShapes.map { it.toFlashcard() }
)
