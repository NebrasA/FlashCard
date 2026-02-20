package org.nebras.flashcard.ui.study

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.nebras.flashcard.data.DeckRepository
import org.nebras.flashcard.data.Flashcard

class StudyViewModel(deckTitle: String) : ViewModel() {

    private val deck = DeckRepository.allDecks.first { it.title == deckTitle }

    private val _cards = MutableStateFlow(deck.cards.shuffled())
    val cards: StateFlow<List<Flashcard>> = _cards.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isFlipped = MutableStateFlow(false)
    val isFlipped: StateFlow<Boolean> = _isFlipped.asStateFlow()

    val totalCards: Int get() = _cards.value.size

    fun flip() {
        _isFlipped.value = !_isFlipped.value
    }

    /** Returns true if advanced past the last card (signals navigation to Completion). */
    fun next(): Boolean {
        if (_currentIndex.value < totalCards - 1) {
            _currentIndex.value++
            _isFlipped.value = false
            return false
        }
        return true
    }

    fun previous() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
            _isFlipped.value = false
        }
    }
}
