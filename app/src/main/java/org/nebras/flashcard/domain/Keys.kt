package org.nebras.flashcard.domain

val CMajor = MusicalKey(
    name = "C Major",
    notes = listOf(Note.C, Note.D, Note.E, Note.F, Note.G, Note.A, Note.B),
    triads = listOf(
        Triad(Note.C, Note.E, Note.G, ChordQuality.MAJOR),
        Triad(Note.D, Note.F, Note.A, ChordQuality.MINOR),
        Triad(Note.E, Note.G, Note.B, ChordQuality.MINOR),
        Triad(Note.F, Note.A, Note.C, ChordQuality.MAJOR),
        Triad(Note.G, Note.B, Note.D, ChordQuality.MAJOR),
        Triad(Note.A, Note.C, Note.E, ChordQuality.MINOR),
        Triad(Note.B, Note.D, Note.F, ChordQuality.DIMINISHED)
    )
)
