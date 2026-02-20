package org.nebras.flashcard.domain

import org.nebras.flashcard.data.CardContent
import org.nebras.flashcard.data.DotType
import org.nebras.flashcard.data.Flashcard
import org.nebras.flashcard.data.FretboardDot

data class TriadShape(
    val quality: ChordQuality,
    val inversion: Inversion,
    val stringGroup: StringGroup,
    val dots: List<ShapeDot>
) {
    fun toFlashcard(): Flashcard {
        val fretboardDots = dots.map { dot ->
            FretboardDot(
                string = dot.string,
                fret = dot.relativeOffset + 2,
                type = if (dot.intervalLabel == "R") DotType.ROOT else DotType.INTERVAL,
                label = dot.intervalLabel
            )
        }
        val label = "${quality.displayName} ${inversion.displayName}"
        return Flashcard(
            question = label,
            answer = label,
            answerContent = CardContent.FretboardDiagram(
                textLabel = label,
                dots = fretboardDots,
                startFret = 1,
                fretCount = 5
            )
        )
    }
}

data class ShapeDot(
    val string: Int,        // 1=high E, 2=B, 3=G
    val relativeOffset: Int,
    val intervalLabel: String   // "R", "3", "5", "♭3", etc.
)

enum class Inversion(val displayName: String) {
    ROOT("Root Position"),
    FIRST("1st Inversion"),
    SECOND("2nd Inversion")
}

enum class StringGroup { GBE }
