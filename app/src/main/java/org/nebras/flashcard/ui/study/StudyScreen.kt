package org.nebras.flashcard.ui.study

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.nebras.flashcard.data.CardContent
import org.nebras.flashcard.ui.components.Fretboard
import org.nebras.flashcard.ui.theme.AnswerCardDark
import org.nebras.flashcard.ui.theme.AnswerCardLight
import org.nebras.flashcard.ui.theme.QuestionCardDark
import org.nebras.flashcard.ui.theme.QuestionCardLight
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    deckTitle: String,
    onComplete: () -> Unit,
    onGoHome: () -> Unit,
    viewModel: StudyViewModel = viewModel { StudyViewModel(deckTitle) }
) {
    val cards by viewModel.cards.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val isFlipped by viewModel.isFlipped.collectAsState()
    val currentCard = cards[currentIndex]

    val isDark = isSystemInDarkTheme()
    val cardContentColor = if (isDark) Color(0xFFE0E0E0) else Color(0xFF1C1B1F)

    // Flip animation: 0° = question face, 180° = answer face
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "cardFlip"
    )
    val showingAnswer = rotation > 90f
    val cardColor = if (showingAnswer) {
        if (isDark) AnswerCardDark else AnswerCardLight
    } else {
        if (isDark) QuestionCardDark else QuestionCardLight
    }

    // Slide animation: single card slides out then back in
    val slideOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val slideDistance = screenWidthPx * 0.25f

    fun animateSlide(direction: Int, changeCard: () -> Unit) {
        scope.launch {
            // Slide out in the given direction
            slideOffset.animateTo(
                targetValue = -direction * slideDistance,
                animationSpec = tween(100)
            )
            // Swap the card content
            changeCard()
            // Jump to the opposite side instantly
            slideOffset.snapTo(direction * slideDistance)
            // Slide back to center
            slideOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(100)
            )
        }
    }

    val goNext: () -> Unit = {
        animateSlide(1) {
            if (viewModel.next()) onComplete()
        }
    }
    val goPrevious: () -> Unit = {
        animateSlide(-1) {
            viewModel.previous()
        }
    }

    var cumulativeDrag by remember { mutableFloatStateOf(0f) }
    val dragThreshold = 100f

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onGoHome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to home"
                        )
                    }
                },
                title = { Text(deckTitle) },
                actions = {
                    Text(
                        text = "${currentIndex + 1} / ${viewModel.totalCards}",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { goPrevious() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous card"
                    )
                }
                IconButton(onClick = { goNext() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next card"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { cumulativeDrag = 0f },
                        onDragEnd = {
                            if (cumulativeDrag < -dragThreshold) {
                                goNext()
                            } else if (cumulativeDrag > dragThreshold) {
                                goPrevious()
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            cumulativeDrag += dragAmount
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .padding(24.dp)
                    .offset { IntOffset(slideOffset.value.roundToInt(), 0) }
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { viewModel.flip() },
                colors = CardDefaults.cardColors(
                    containerColor = cardColor,
                    contentColor = cardContentColor
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = if (showingAnswer) 180f else 0f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (showingAnswer && currentCard.answerContent != null) {
                        when (val content = currentCard.answerContent!!) {
                            is CardContent.FretboardDiagram -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = content.textLabel,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Fretboard(
                                        dots = content.dots,
                                        startFret = content.startFret,
                                        fretCount = content.fretCount,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                }
                            }
                            is CardContent.Text -> {
                                Text(
                                    text = content.value,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }
                    } else {
                        Text(
                            text = if (showingAnswer) currentCard.answer else currentCard.question,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
        }
    }
}
