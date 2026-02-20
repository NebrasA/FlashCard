package org.nebras.flashcard.data

import org.nebras.flashcard.domain.CMajor

object DeckRepository {
    val allDecks: List<Deck> = listOf(
        CMajor.toDeck()
    )
}
