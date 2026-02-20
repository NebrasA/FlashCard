package org.nebras.flashcard.data

data class Flashcard(
    val question: String,
    val answer: String,
    val answerContent: CardContent? = null
)

data class Deck(
    val title: String,
    val cards: List<Flashcard>
)
