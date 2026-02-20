package org.nebras.flashcard.domain

enum class Note(val semitone: Int) {
    C(0), D(2), E(4), F(5), G(7), A(9), B(11)
}

// Open string semitones in standard tuning: 1=high E, 2=B, 3=G
private val openStringSemitones = mapOf(1 to 4, 2 to 11, 3 to 7)

/**
 * Calculate the fret where [note] falls on the given [string].
 * Returns a fret in 1..12 (fret 0 maps to 12 to avoid open strings).
 */
fun fretFor(note: Note, string: Int): Int {
    val open = openStringSemitones[string] ?: error("Unsupported string: $string")
    val fret = (note.semitone - open + 12) % 12
    return if (fret == 0) 12 else fret
}
