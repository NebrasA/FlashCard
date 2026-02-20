package org.nebras.flashcard.ui.home

import androidx.lifecycle.ViewModel
import org.nebras.flashcard.data.Deck
import org.nebras.flashcard.data.DeckRepository

class HomeViewModel : ViewModel() {
    val decks: List<Deck> = DeckRepository.allDecks
}
