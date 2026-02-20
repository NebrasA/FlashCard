package org.nebras.flashcard.data

import org.nebras.flashcard.domain.CMajor
import org.nebras.flashcard.domain.modeShapesDeck
import org.nebras.flashcard.domain.triadShapes321Deck

object DeckRepository {
    val allDecks: List<Deck> = listOf(
        CMajor.toDeck(),
        triadShapes321Deck(),
        modeShapesDeck()
    )
}
