package com.vibecode.daosubatma.feature.talisman

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.vibecode.daosubatma.ui.Danger
import com.vibecode.daosubatma.ui.Gold
import kotlin.math.min

@Composable
fun TalismanCanvas(
    path: List<Offset>,
    onPathChanged: (List<Offset>) -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    onDrawingChanged: (Boolean) -> Unit,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val latestPath by rememberUpdatedState(path)
    Canvas(modifier.onSizeChanged(onSizeChanged).pointerInput(Unit) {
        detectDragGestures(
            onDragStart = { start -> onPathChanged(listOf(start)); onDrawingChanged(true) },
            onDrag = { change, _ -> onPathChanged(latestPath + change.position) },
            onDragEnd = { onDrawingChanged(false); onFinished() },
            onDragCancel = { onDrawingChanged(false) },
        )
    }) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = min(size.width, size.height) * .30f
        drawCircle(Gold.copy(alpha = .25f), radius + 23f, center, style = Stroke(width = 3f))
        drawCircle(Gold.copy(alpha = .92f), radius, center, style = Stroke(width = 12f))
        drawCircle(Gold.copy(alpha = .55f), radius - 30f, center, style = Stroke(width = 2f))
        repeat(12) { index ->
            val angle = index * (Math.PI * 2 / 12)
            val start = Offset(
                center.x + kotlin.math.cos(angle).toFloat() * (radius + 8f),
                center.y + kotlin.math.sin(angle).toFloat() * (radius + 8f),
            )
            val end = Offset(
                center.x + kotlin.math.cos(angle).toFloat() * (radius + 27f),
                center.y + kotlin.math.sin(angle).toFloat() * (radius + 27f),
            )
            drawLine(Gold.copy(alpha = .9f), start, end, strokeWidth = 5f)
        }
        val talismanSize = Size(radius * .46f, radius * .68f)
        val talismanTopLeft = Offset(center.x - talismanSize.width / 2, center.y - talismanSize.height / 2)
        drawRoundRect(Gold.copy(alpha = .24f), talismanTopLeft, talismanSize, CornerRadius(16f, 16f))
        drawRoundRect(Gold.copy(alpha = .92f), talismanTopLeft, talismanSize, CornerRadius(16f, 16f), style = Stroke(width = 5f))
        if (path.size > 1) {
            val drawn = Path().apply {
                moveTo(path.first().x, path.first().y)
                path.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(drawn, Danger, style = Stroke(width = 15f))
        }
    }
}
