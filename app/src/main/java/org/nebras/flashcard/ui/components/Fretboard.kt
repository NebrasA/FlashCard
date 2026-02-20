package org.nebras.flashcard.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import org.nebras.flashcard.data.DotType
import org.nebras.flashcard.data.FretboardDot

@Composable
fun Fretboard(
    dots: List<FretboardDot>,
    startFret: Int = 0,
    fretCount: Int = 5,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val stringColor = if (isDark) Color(0xFFBDBDBD) else Color(0xFF616161)
    val fretColor = if (isDark) Color(0xFF9E9E9E) else Color(0xFF757575)
    val nutColor = if (isDark) Color(0xFFE0E0E0) else Color(0xFF212121)
    val rootDotColor = Color(0xFFE53935)
    val intervalDotColor = if (isDark) Color(0xFF90CAF9) else Color(0xFF1565C0)
    val fretNumberColor = if (isDark) Color(0xFF9E9E9E) else Color(0xFF757575)
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Layout: left-handed = nut on RIGHT side
        val marginLeft = width * 0.08f
        val marginRight = width * 0.06f
        val marginTop = height * 0.10f
        val marginBottom = height * 0.18f

        val fretboardLeft = marginLeft
        val fretboardRight = width - marginRight
        val fretboardTop = marginTop
        val fretboardBottom = height - marginBottom
        val fretboardWidth = fretboardRight - fretboardLeft
        val fretboardHeight = fretboardBottom - fretboardTop

        val stringCount = 6
        val stringSpacing = fretboardHeight / (stringCount - 1)
        val fretSpacing = fretboardWidth / fretCount

        // Draw strings (high E at top, low E at bottom)
        for (i in 0 until stringCount) {
            val y = fretboardTop + i * stringSpacing
            drawLine(
                color = stringColor,
                start = Offset(fretboardLeft, y),
                end = Offset(fretboardRight, y),
                strokeWidth = 2f + i * 0.5f
            )
        }

        // Draw fret lines
        for (i in 0..fretCount) {
            val x = fretboardLeft + i * fretSpacing
            val isNut = (startFret == 0 && i == fretCount) ||
                    (startFret == 1 && i == fretCount)
            drawLine(
                color = if (isNut) nutColor else fretColor,
                start = Offset(x, fretboardTop),
                end = Offset(x, fretboardBottom),
                strokeWidth = if (isNut) 6f else 2f,
                cap = StrokeCap.Round
            )
        }

        // Draw fret numbers below the fretboard (left-handed: fret 1 near nut on right)
        val fretNumberStyle = TextStyle(
            color = fretNumberColor,
            fontSize = 11.sp
        )
        for (i in 0 until fretCount) {
            val actualFret = if (startFret == 0) i else startFret + i
            if (actualFret == 0) continue
            val centerX = fretboardRight - (i * fretSpacing) - fretSpacing / 2
            drawFretNumber(
                textMeasurer = textMeasurer,
                text = actualFret.toString(),
                style = fretNumberStyle,
                centerX = centerX,
                y = fretboardBottom + 14f
            )
        }

        // Draw dots with interval labels
        val dotRadius = minOf(stringSpacing, fretSpacing) * 0.28f
        val dotLabelSizeSp = (dotRadius * 0.9f / density).sp
        val dotLabelStyle = TextStyle(
            color = Color.White,
            fontSize = dotLabelSizeSp
        )
        for (dot in dots) {
            val stringIndex = dot.string - 1  // 1-based to 0-based
            val y = fretboardTop + stringIndex * stringSpacing

            // For left-handed: fret 1 is rightmost, higher frets go left
            // Dot sits between fret lines, centered in the fret space
            val fretIndex = dot.fret - (if (startFret == 0) 0 else startFret)
            val x = fretboardRight - (fretIndex * fretSpacing) - fretSpacing / 2

            val color = when (dot.type) {
                DotType.ROOT -> rootDotColor
                DotType.INTERVAL -> intervalDotColor
            }

            drawCircle(
                color = color,
                radius = dotRadius,
                center = Offset(x, y)
            )

            if (dot.label.isNotEmpty()) {
                val labelLayout = textMeasurer.measure(dot.label, dotLabelStyle)
                drawText(
                    textLayoutResult = labelLayout,
                    topLeft = Offset(
                        x = x - labelLayout.size.width / 2f,
                        y = y - labelLayout.size.height / 2f
                    )
                )
            }
        }
    }
}

private fun DrawScope.drawFretNumber(
    textMeasurer: TextMeasurer,
    text: String,
    style: TextStyle,
    centerX: Float,
    y: Float
) {
    val layoutResult = textMeasurer.measure(text, style)
    drawText(
        textLayoutResult = layoutResult,
        topLeft = Offset(
            x = centerX - layoutResult.size.width / 2f,
            y = y
        )
    )
}
