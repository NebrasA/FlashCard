package org.nebras.flashcard.domain

import org.nebras.flashcard.data.Flashcard

data class Triad(
    val root: Note,
    val third: Note,
    val fifth: Note,
    val quality: ChordQuality
) {
    fun toFlashcards(): List<Flashcard> = listOf(
        Flashcard("Root inversion ${label()}", "$root $third $fifth"),
        Flashcard("1st inversion ${label()}", "$third $fifth $root"),
        Flashcard("2nd inversion ${label()}", "$fifth $root $third")
    )

    internal fun label(): String = when (quality) {
        ChordQuality.MAJOR -> root.name
        ChordQuality.MINOR -> "${root.name}m"
        ChordQuality.DIMINISHED -> "${root.name}dim"
    }
}
