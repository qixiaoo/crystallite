package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import io.github.qixiaoo.crystallite.ui.common.Ref
import io.github.qixiaoo.crystallite.ui.common.dpToPx
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun HorizontalPager(
    modifier: Modifier = Modifier,
    currentPage: Int = 0,
    pageCount: Int,
    onClick: () -> Unit = {},
    onPageChange: (Int) -> Unit = {},
    pageKey: ((page: Int) -> Any)? = null,
    pageContent: @Composable (page: Int) -> Unit,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenWidthPx = screenWidthDp.dpToPx(LocalContext.current)

    val offsetX = remember {
        Animatable(0f)
    }

    val deltaXRef = remember {
        Ref(0f)
    }

    val coroutineScope = rememberCoroutineScope()

    val current by rememberUpdatedState(newValue = currentPage)

    val direction by rememberUpdatedState(LocalLayoutDirection.current)

    val updatedBounds by rememberUpdatedState(
        calculateOffsetBounds(
            current = current,
            pageCount = pageCount,
            screenWidthPx = screenWidthPx,
            direction = direction
        )
    )

    // the `HorizontalPager` only displays the previous、current and next pages
    val from = (current - 1).coerceAtLeast(0)
    val to = (current + 1).coerceAtMost(pageCount - 1)
    val pageSlice = from..to

    Layout(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalPagerDragGestures(
                    onHorizontalDrag = { _, delta ->
                        coroutineScope.launch {
                            val (lowerBound, upperBound) = updatedBounds
                            deltaXRef.value += delta
                            val nextOffsetX = (offsetX.value + delta)
                                .coerceAtLeast(lowerBound)
                                .coerceAtMost(upperBound)
                            offsetX.snapTo(nextOffsetX)
                        }
                    },
                    onDragEnd = { velocity ->
                        coroutineScope.launch {
                            val (lowerBound, upperBound) = updatedBounds
                            val (nextOffsetX, deltaPage) = calculateNextOffsetXAndDeltaPage(
                                deltaX = deltaXRef.value,
                                velocityX = velocity,
                                direction = direction,
                                screenWidthPx = screenWidthPx,
                            )
                            val coercedOffsetX = nextOffsetX
                                .coerceAtLeast(lowerBound)
                                .coerceAtMost(upperBound)
                            val coercedCurrent = (current + deltaPage)
                                .coerceAtLeast(0)
                                .coerceAtMost(pageCount - 1)

                            offsetX.animateTo(coercedOffsetX)
                            onPageChange(coercedCurrent)
                            deltaXRef.value = 0f
                            offsetX.snapTo(0f)
                        }
                    },
                    onClick = onClick
                )
            }
            .then(modifier),

        content = {
            pageSlice.forEach { pageIndex ->
                val keyOfPage = pageKey?.let { it(pageIndex) } ?: pageIndex
                key(keyOfPage) {
                    pageContent(pageIndex)
                }
            }
        }) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val x = calculatePositionX(
                    index = index,
                    current = current,
                    screenWidthPx = screenWidthPx,
                    direction = direction,
                    offsetX = offsetX.value
                )
                placeable.place(x = x, y = 0)
            }
        }
    }
}

/**
 * calculate the x position where the page should be placed
 */
private fun calculatePositionX(
    index: Int,
    current: Int,
    screenWidthPx: Float,
    direction: LayoutDirection,
    offsetX: Float,
): Int {
    val currentIndex = if (current == 0) 0 else 1
    val xPosition = when (direction) {
        LayoutDirection.Ltr -> (index - currentIndex) * screenWidthPx
        LayoutDirection.Rtl -> (currentIndex - index) * screenWidthPx
    }
    return (xPosition + offsetX).roundToInt()
}

/**
 * calculate the offsetX where the dragging page should snap to
 */
private fun calculateNextOffsetXAndDeltaPage(
    deltaX: Float,
    velocityX: Float,
    direction: LayoutDirection,
    screenWidthPx: Float,
): Pair<Float, Int> {
    val thresholdDeltaX = screenWidthPx / 2
    val thresholdVelocityX = 2000

    when (direction) {
        LayoutDirection.Ltr -> {
            if (deltaX < -thresholdDeltaX || velocityX < -thresholdVelocityX) {
                return Pair(-screenWidthPx, 1)
            }
            if (deltaX > thresholdDeltaX || velocityX > thresholdVelocityX) {
                return Pair(screenWidthPx, -1)
            }
        }

        LayoutDirection.Rtl -> {
            if (deltaX < -thresholdDeltaX || velocityX < -thresholdVelocityX) {
                return Pair(-screenWidthPx, -1)
            }
            if (deltaX > thresholdDeltaX || velocityX > thresholdVelocityX) {
                return Pair(screenWidthPx, 1)
            }
        }
    }

    return Pair(0f, 0)
}

/**
 * calculate the lower and upper bounds of the page offsetX when dragging
 */
private fun calculateOffsetBounds(
    current: Int,
    pageCount: Int,
    screenWidthPx: Float,
    direction: LayoutDirection,
): Pair<Float, Float> {
    val defaultLowerBound = -screenWidthPx
    val defaultUpperBound = screenWidthPx

    val isFirstPage = current == 0
    val isLastPage = current == pageCount - 1

    when (direction) {
        LayoutDirection.Ltr -> {
            if (isFirstPage) {
                return Pair(defaultLowerBound, 0f)
            }
            if (isLastPage) {
                return Pair(0f, defaultUpperBound)
            }
        }

        LayoutDirection.Rtl -> {
            if (isFirstPage) {
                return Pair(0f, defaultUpperBound)
            }
            if (isLastPage) {
                return Pair(defaultLowerBound, 0f)
            }
        }
    }

    return Pair(defaultLowerBound, defaultUpperBound)
}
