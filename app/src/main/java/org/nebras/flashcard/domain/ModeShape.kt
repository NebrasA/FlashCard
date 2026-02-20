package org.nebras.flashcard.domain

import org.nebras.flashcard.data.CardContent
import org.nebras.flashcard.data.DotType
import org.nebras.flashcard.data.Flashcard
import org.nebras.flashcard.data.FretboardDot

data class ModeShape(
    val name: String,
    val rootDegree: Int, // 1=Ionian, 2=Dorian, 3=Phrygian, etc.
    val dots: List<ModeDot>
) {
    fun toFlashcard(): Flashcard {
        val displayOffset = 3 // shift so root (offset 0) displays at fret 3
        val fretboardDots = dots.map { dot ->
            FretboardDot(
                string = dot.string,
                fret = dot.relativeOffset + displayOffset,
                type = if (dot.degreeLabel == "R") DotType.ROOT else DotType.INTERVAL,
                label = dot.degreeLabel
            )
        }
        val maxFret = fretboardDots.maxOf { it.fret }
        return Flashcard(
            question = name,
            answer = name,
            answerContent = CardContent.FretboardDiagram(
                textLabel = name,
                dots = fretboardDots,
                startFret = 1,
                fretCount = maxFret + 1
            )
        )
    }
}

data class ModeDot(
    val string: Int,         // 1=high E .. 6=low E
    val relativeOffset: Int, // semitone offset from root on low E
    val degreeLabel: String  // "1" through "7"
)
