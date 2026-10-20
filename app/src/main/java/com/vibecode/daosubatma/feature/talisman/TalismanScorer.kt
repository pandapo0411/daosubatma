package com.vibecode.daosubatma.feature.talisman

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.sqrt

fun scoreCircle(points: List<Offset>, area: IntSize): Float {
    if (points.size < 12 || area == IntSize.Zero) return 0f
    val center = Offset(area.width / 2f, area.height / 2f)
    val expectedRadius = min(area.width, area.height) * .30f
    val radialError = points.map { point ->
        val dx = point.x - center.x
        val dy = point.y - center.y
        abs(sqrt(dx * dx + dy * dy) - expectedRadius) / expectedRadius
    }.average().toFloat()
    val angles = points.map { atan2(it.y - center.y, it.x - center.x) }.sorted()
    val largestGap = angles.zipWithNext().maxOfOrNull { (a, b) -> b - a } ?: (2f * PI.toFloat())
    val wrapGap = angles.first() + 2f * PI.toFloat() - angles.last()
    val coverage = 1f - maxOf(largestGap, wrapGap) / (2f * PI.toFloat())
    return ((1f - radialError * 2.25f).coerceIn(0f, 1f) * .72f + coverage.coerceIn(0f, 1f) * .28f).coerceIn(0f, 1f)
}
