# Flashcard App — Design Document

This project will be a simple flashcard application that allows the user to view decks of flashcards
and flip through them on a mobile device.

Because this will only be used as a personal project, we will be hardcoding these decks rather than
implementing functionality to create custom flashcards. The content of these decks will be focused
on guitar / musical study material.

---

## Architecture

### Platform & Tech Stack

- **Platform:** Android only
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Navigation:** Jetpack Navigation Compose (`NavHost` + `NavController`)

### Project Structure

```
com.example.flashcard/
├── data/          # Flashcard, Deck — UI models
├── domain/        # Note, ChordQuality, Triad, MusicalKey + key constants
├── ui/
│   ├── home/      # HomeScreen composable + HomeViewModel
│   ├── study/     # StudyScreen composable + StudyViewModel
│   ├── completion/ # CompletionScreen composable
│   └── theme/     # Colors, typography, Material theme
└── MainActivity.kt
```

### Screens & Navigation

Navigation is handled by Jetpack Navigation Compose. The nav graph has three destinations: Home, Study, and Completion.

#### Home Screen
- Displays a list of available decks
- Tapping a deck navigates to the Study Screen for that deck
- Only one deck exists currently; more will be added in future

#### Study Screen
- Displays one card at a time, full screen
- Shows the **question** by default
- **Tapping** the card flips it to reveal the answer
- **Swiping left** or pressing a **right arrow** advances to the next card
- **Swiping right** or pressing a **left arrow** returns to the previous card
- The **upper-right corner** displays current card position (e.g. `3 / 22`)
- Advancing past the last card navigates to the Completion Screen

#### Completion Screen
- Shown when the user advances past the last card in a deck
- Displays a message indicating the deck is complete
- Offers two actions:
  - **Reshuffle & Restart** — reshuffles the deck and returns to card 1 in the Study Screen
  - **Go Home** — navigates back to the Home Screen

### State Management

Each screen has a dedicated `ViewModel` to survive recomposition and configuration changes (e.g. screen rotation).

- **`HomeViewModel`** — owns the list of available decks
- **`StudyViewModel`** — owns the shuffled card list for the session, current card index, and current card flip state

### Deck Behavior

- Card order is **randomized** at the start of each session
- Returning to a deck always starts from card 1 with a fresh shuffle

### Data Model

Card data is hardcoded as Kotlin data classes and constants. No external files or parsing.

There are two layers:

1. **Domain models** — structured representations of the musical content, responsible for generating their own flashcards
2. **UI models** — simple `Flashcard`/`Deck` types consumed by the UI, with no knowledge of music theory

#### UI Models

```kotlin
data class Flashcard(
    val question: String,
    val answer: String
)

data class Deck(
    val title: String,
    val cards: List<Flashcard>
)
```

#### Domain Models

```kotlin
enum class Note { C, D, E, F, G, A, B }

enum class ChordQuality { MAJOR, MINOR, DIMINISHED }

data class Triad(
    val root: Note,
    val third: Note,
    val fifth: Note,
    val quality: ChordQuality
) {
    // Derives root, 1st, and 2nd inversion flashcards
    fun toFlashcards(): List<Flashcard> = listOf(
        Flashcard("Root inversion ${label()}", "${root} ${third} ${fifth}"),
        Flashcard("1st inversion ${label()}", "${third} ${fifth} ${root}"),
        Flashcard("2nd inversion ${label()}", "${fifth} ${root} ${third}")
    )

    private fun label(): String = when (quality) {
        ChordQuality.MAJOR -> root.name
        ChordQuality.MINOR -> "${root.name}m"
        ChordQuality.DIMINISHED -> "${root.name}dim"
    }
}

data class MusicalKey(
    val name: String,
    val notes: List<Note>,
    val triads: List<Triad>
) {
    fun toDeck(): Deck = Deck(
        title = name,
        cards = listOf(
            Flashcard(
                "What are the notes and chords of $name?",
                "Notes: ${notes.joinToString(" ")} | Chords: ${triads.joinToString(", ") { it.label() }}"
            )
        ) + triads.flatMap { it.toFlashcards() }
    )
}
```

#### Data Layer

Each key is defined as a constant. The `toDeck()` call generates all flashcards automatically — no need to manually write out each inversion.

```kotlin
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
```

---

## Deck 1: Keys & Chords

This deck will contain questions regarding the makeup of a particular musical key. For example,
in C Major:

- The notes that make up the key are: C D E F G A B
- Using the standard formula for major and minor chords, the chords in this key are: C major, D minor, E minor, F major, G major, A minor, B diminished
- The various triads and inversions to build a chord in the key using the root, 3rd, and 5th intervals. For example:
  - D Major root (R/3/5) → D F A
  - D Major 1st inversion (3/5/R) → F A D
  - D Major 2nd inversion (5/R/3) → A D F

For now this deck will only focus on the key of C Major.

### Flashcards

**Q: What are the notes that make up C Major and what are the major and minor chords?**
A: C, Dm, Em, F, G, Am, Bdim

---

#### C Major Triads

| Inversion | Question          | Answer  |
|-----------|-------------------|---------|
| Root      | Root inversion C  | C E G   |
| 1st       | 1st inversion C   | E G C   |
| 2nd       | 2nd inversion C   | G C E   |

#### D Minor Triads

| Inversion | Question          | Answer  |
|-----------|-------------------|---------|
| Root      | Root inversion Dm | D F A   |
| 1st       | 1st inversion Dm  | F A D   |
| 2nd       | 2nd inversion Dm  | A D F   |

#### E Minor Triads

| Inversion | Question          | Answer  |
|-----------|-------------------|---------|
| Root      | Root inversion Em | E G B   |
| 1st       | 1st inversion Em  | G B E   |
| 2nd       | 2nd inversion Em  | B E G   |

#### F Major Triads

| Inversion | Question          | Answer  |
|-----------|-------------------|---------|
| Root      | Root inversion F  | F A C   |
| 1st       | 1st inversion F   | A C F   |
| 2nd       | 2nd inversion F   | C F A   |

#### G Major Triads

| Inversion | Question          | Answer  |
|-----------|-------------------|---------|
| Root      | Root inversion G  | G B D   |
| 1st       | 1st inversion G   | B D G   |
| 2nd       | 2nd inversion G   | D G B   |

#### A Minor Triads

| Inversion | Question          | Answer  |
|-----------|-------------------|---------|
| Root      | Root inversion Am | A C E   |
| 1st       | 1st inversion Am  | C E A   |
| 2nd       | 2nd inversion Am  | E A C   |

#### B Diminished Triads

| Inversion | Question            | Answer  |
|-----------|---------------------|---------|
| Root      | Root inversion Bdim | B D F   |
| 1st       | 1st inversion Bdim  | D F B   |
| 2nd       | 2nd inversion Bdim  | F B D   |
