package org.nebras.flashcard.domain

import org.nebras.flashcard.data.Deck

private val majorRoot = TriadShape(
    quality = ChordQuality.MAJOR,
    inversion = Inversion.ROOT,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 2, intervalLabel = "R"),
        ShapeDot(string = 2, relativeOffset = 2, intervalLabel = "3"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "5")
    )
)

private val majorFirst = TriadShape(
    quality = ChordQuality.MAJOR,
    inversion = Inversion.FIRST,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 1, intervalLabel = "3"),
        ShapeDot(string = 2, relativeOffset = 0, intervalLabel = "5"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "R")
    )
)

private val majorSecond = TriadShape(
    quality = ChordQuality.MAJOR,
    inversion = Inversion.SECOND,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 0, intervalLabel = "5"),
        ShapeDot(string = 2, relativeOffset = 1, intervalLabel = "R"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "3")
    )
)

private val minorRoot = TriadShape(
    quality = ChordQuality.MINOR,
    inversion = Inversion.ROOT,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 2, intervalLabel = "R"),
        ShapeDot(string = 2, relativeOffset = 1, intervalLabel = "♭3"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "5")
    )
)

private val minorFirst = TriadShape(
    quality = ChordQuality.MINOR,
    inversion = Inversion.FIRST,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 0, intervalLabel = "♭3"),
        ShapeDot(string = 2, relativeOffset = 0, intervalLabel = "5"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "R")
    )
)

private val minorSecond = TriadShape(
    quality = ChordQuality.MINOR,
    inversion = Inversion.SECOND,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 1, intervalLabel = "5"),
        ShapeDot(string = 2, relativeOffset = 2, intervalLabel = "R"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "♭3")
    )
)

private val dimRoot = TriadShape(
    quality = ChordQuality.DIMINISHED,
    inversion = Inversion.ROOT,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 3, intervalLabel = "R"),
        ShapeDot(string = 2, relativeOffset = 2, intervalLabel = "♭3"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "♭5")
    )
)

private val dimFirst = TriadShape(
    quality = ChordQuality.DIMINISHED,
    inversion = Inversion.FIRST,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 1, intervalLabel = "♭3"),
        ShapeDot(string = 2, relativeOffset = 0, intervalLabel = "♭5"),
        ShapeDot(string = 1, relativeOffset = 1, intervalLabel = "R")
    )
)

private val dimSecond = TriadShape(
    quality = ChordQuality.DIMINISHED,
    inversion = Inversion.SECOND,
    stringGroup = StringGroup.GBE,
    dots = listOf(
        ShapeDot(string = 3, relativeOffset = 0, intervalLabel = "♭5"),
        ShapeDot(string = 2, relativeOffset = 2, intervalLabel = "R"),
        ShapeDot(string = 1, relativeOffset = 0, intervalLabel = "♭3")
    )
)

private val allTriadShapes = listOf(
    majorRoot, majorFirst, majorSecond,
    minorRoot, minorFirst, minorSecond,
    dimRoot, dimFirst, dimSecond
)

fun triadShapes321Deck(): Deck = Deck(
    title = "Triad Shapes (3-2-1)",
    cards = allTriadShapes.map { it.toFlashcard() }
)
