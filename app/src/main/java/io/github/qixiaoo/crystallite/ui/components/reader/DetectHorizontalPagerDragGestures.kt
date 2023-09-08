package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import io.github.qixiaoo.crystallite.ui.common.radToDeg
import kotlin.math.abs
import kotlin.math.atan

/**
 * detect drag gesture for the `HorizontalPager`
 */
suspend fun PointerInputScope.detectHorizontalPagerDragGestures(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: (velocity: Float) -> Unit = { },
    onDragCancel: () -> Unit = { },
    onHorizontalDrag: (change: PointerInputChange, dragAmount: Float) -> Unit,
    onClick: () -> Unit = { },
) {
    awaitEachGesture {
        val firstDown = awaitFirstDown(requireUnconsumed = false)
        val firstDownPosition = firstDown.position

        var overSlop = 0f
        val thresholdDegree = 15

        // detect a drag start or cancel
        val dragStart = awaitTouchSlopOrCancellation(firstDown.id) { change, over ->
            val c = change.positionChange()
            val tan = c.y / c.x
            val rad = atan(tan)
            val degree = abs(radToDeg(rad.toDouble()))

            // only trigger drag behavior when the `degree` less than the `thresholdDegree`,
            // so that zooming image doesn't lead to turning page by accident
            if (degree < thresholdDegree) {
                change.consume()
                overSlop = over.x
            }
        }

        if (dragStart != null) {
            // when drag triggered, invoke relevant lambda callbacks
            onDragStart.invoke(dragStart.position)
            onHorizontalDrag(dragStart, overSlop)

            val velocityTracker = VelocityTracker()
            velocityTracker.addPosition(dragStart.uptimeMillis, dragStart.position)

            val dragEnd = horizontalDrag(dragStart.id) {
                onHorizontalDrag(it, it.positionChange().x)
                velocityTracker.addPosition(it.uptimeMillis, it.position)
                it.consume()
            }

            if (dragEnd) {
                onDragEnd(velocityTracker.calculateVelocity().x)
            } else {
                onDragCancel()
            }
        } else {
            // when drag cancelled, await last up event and save its position
            val lastUpEvent = awaitPointerEvent(pass = PointerEventPass.Final)
            var lastUpPosition = Offset.Zero
            if (lastUpEvent.type == PointerEventType.Release) {
                lastUpEvent.changes.getOrNull(0)?.let {
                    lastUpPosition = it.position
                }
            }

            // to avoid triggering `onClick` twice when double tapping,
            // let us await and ignore the second down event
            val doubleTapped = withTimeoutOrNull(viewConfiguration.doubleTapTimeoutMillis) {
                awaitFirstDown(requireUnconsumed = false)
            }

            // if there is no drag motion between firstDown and lastUp, then trigger `onClick`
            if (doubleTapped == null && firstDownPosition == lastUpPosition) {
                onClick()
            }
        }
    }
}