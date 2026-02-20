package org.nebras.flashcard.domain

import org.nebras.flashcard.data.CardContent
import org.nebras.flashcard.data.DotType
import org.nebras.flashcard.data.Flashcard
import org.nebras.flashcard.data.FretboardDot

data class Triad(
    val root: Note,
    val third: Note,
    val fifth: Note,
    val quality: ChordQuality
) {
    fun toFlashcards(): List<Flashcard> {
        // Each inversion: (name, answerText, notes-on-strings)
        // String assignment bass-to-treble: G=3, B=2, E=1
        val inversions = listOf(
            Triple(
                "Root inversion", "$root $third $fifth", listOf(
                    NoteOnString(root, TriadRole.ROOT, 3),
                    NoteOnString(third, TriadRole.THIRD, 2),
                    NoteOnString(fifth, TriadRole.FIFTH, 1)
                )
            ),
            Triple(
                "1st inversion", "$third $fifth $root", listOf(
                    NoteOnString(third, TriadRole.THIRD, 3),
                    NoteOnString(fifth, TriadRole.FIFTH, 2),
                    NoteOnString(root, TriadRole.ROOT, 1)
                )
            ),
            Triple(
                "2nd inversion", "$fifth $root $third", listOf(
                    NoteOnString(fifth, TriadRole.FIFTH, 3),
                    NoteOnString(root, TriadRole.ROOT, 2),
                    NoteOnString(third, TriadRole.THIRD, 1)
                )
            )
        )

        return inversions.map { (inversionName, answerText, notesOnStrings) ->
            val rawFrets = notesOnStrings.map { fretFor(it.note, it.string) }

            // Ensure a compact voicing: if span > 4 frets, shift low notes up 12
            val compactFrets = makeCompact(rawFrets)

            val dots = notesOnStrings.zip(compactFrets).map { (nos, fret) ->
                val interval = intervalLabel(quality, nos.role)
                FretboardDot(
                    string = nos.string,
                    fret = fret,
                    type = if (nos.role == TriadRole.ROOT) DotType.ROOT else DotType.INTERVAL,
                    label = "$interval/${nos.note}"
                )
            }

            val frets = dots.map { it.fret }
            val minFret = frets.min()
            val maxFret = frets.max()
            val startFret = maxOf(1, minFret - 1)
            val fretCount = maxOf(4, maxFret - startFret + 2)

            Flashcard(
                question = "$inversionName ${label()}",
                answer = answerText,
                answerContent = CardContent.FretboardDiagram(
                    textLabel = answerText,
                    dots = dots,
                    startFret = startFret,
                    fretCount = fretCount
                )
            )
        }
    }

    internal fun label(): String = when (quality) {
        ChordQuality.MAJOR -> root.name
        ChordQuality.MINOR -> "${root.name}m"
        ChordQuality.DIMINISHED -> "${root.name}dim"
    }
}

private data class NoteOnString(val note: Note, val role: TriadRole, val string: Int)

private enum class TriadRole { ROOT, THIRD, FIFTH }

/**
 * Ensure fret positions form a compact shape (span ≤ 4 frets).
 * If the initial span is too wide, shift the lowest frets up by 12
 * until the shape is compact.
 */
private fun makeCompact(frets: List<Int>): List<Int> {
    val result = frets.toMutableList()
    // Iteratively shift the minimum fret up by 12 until span is ≤ 4
    repeat(3) {
        val span = result.max() - result.min()
        if (span <= 4) return result
        val minVal = result.min()
        val minIdx = result.indexOf(minVal)
        result[minIdx] = minVal + 12
    }
    return result
}

private fun intervalLabel(quality: ChordQuality, role: TriadRole): String = when (role) {
    TriadRole.ROOT -> "R"
    TriadRole.THIRD -> when (quality) {
        ChordQuality.MAJOR -> "3"
        ChordQuality.MINOR, ChordQuality.DIMINISHED -> "\u266D3"
    }
    TriadRole.FIFTH -> when (quality) {
        ChordQuality.MAJOR, ChordQuality.MINOR -> "5"
        ChordQuality.DIMINISHED -> "\u266D5"
    }
}
