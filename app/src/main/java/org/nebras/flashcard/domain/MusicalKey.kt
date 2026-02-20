package org.nebras.flashcard.domain

import org.nebras.flashcard.data.Deck
import org.nebras.flashcard.data.Flashcard

data class MusicalKey(
    val name: String,
    val notes: List<Note>,
    val triads: List<Triad>
) {
    fun toDeck(): Deck = Deck(
        title = name,
        cards = listOf(
            Flashcard(
                question = "What are the notes and chords of $name?",
                answer = "Notes: ${notes.joinToString(" ")} | Chords: ${triads.joinToString(", ") { it.label() }}"
            )
        ) + triads.flatMap { it.toFlashcards() }
    )
}
