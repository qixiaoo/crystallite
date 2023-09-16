package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageNavigator(
    modifier: Modifier = Modifier,
    current: Int,
    pageCount: Int,
    enablePreviousChapter: Boolean = true,
    enableNextChapter: Boolean = true,
    margin: PaddingValues = PaddingValues(),
    onPageChange: (Int) -> Unit = {},
    onNavigateToPrevChapter: () -> Unit = {},
    onNavigateToNextChapter: () -> Unit = {},
) {
    val typography = MaterialTheme.typography

    val steps = if (pageCount > 2) pageCount - 2 else 0
    val pageRange = 0f..(pageCount - 1).toFloat()

    val alpha = 0.9f
    val tonalElevation = 6.dp
    val shape = RoundedCornerShape(50.dp)

    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val prevChapterIcon = if (isLtr) Icons.Default.SkipPrevious else Icons.Default.SkipNext
    val nextChapterIcon = if (isLtr) Icons.Default.SkipNext else Icons.Default.SkipPrevious

    Row(modifier = modifier) {
        // previous chapter button
        Surface(
            tonalElevation = tonalElevation,
            shape = shape,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .alpha(alpha)
        ) {
            PlainTooltipBox(tooltip = { Text(stringResource(R.string.previous_chapter)) }) {
                IconButton(
                    onClick = onNavigateToPrevChapter,
                    enabled = enablePreviousChapter,
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        prevChapterIcon,
                        contentDescription = "previous chapter",
                    )
                }
            }
        }

        // reading progress slider
        Surface(
            tonalElevation = tonalElevation,
            shape = shape,
            modifier = Modifier
                .weight(1f)
                .alpha(alpha)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .padding(margin)
                    .padding(horizontal = 15.dp)
            ) {
                Text(
                    text = (current + 1).toString(),
                    style = typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    steps = steps,
                    value = current.toFloat(),
                    valueRange = pageRange,
                    onValueChange = { onPageChange(it.roundToInt()) },
                    modifier = Modifier
                        .weight(weight = 1f)
                        .semantics { contentDescription = "Page Navigator Slider" }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = pageCount.toString(),
                    style = typography.bodyMedium
                )
            }
        }

        // next chapter button
        Surface(
            tonalElevation = tonalElevation,
            shape = shape,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .alpha(alpha)
        ) {
            PlainTooltipBox(tooltip = { Text(stringResource(R.string.next_chapter)) }) {
                IconButton(
                    onClick = onNavigateToNextChapter,
                    enabled = enableNextChapter,
                    modifier = Modifier.tooltipAnchor()
                ) {
                    Icon(
                        nextChapterIcon,
                        contentDescription = "next chapter",
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PageNavigatorPreview() {
    CrystalliteTheme {
        var current by remember { mutableIntStateOf(0) }
        PageNavigator(
            current = current,
            pageCount = 30,
            onPageChange = { current = it },
            enablePreviousChapter = false
        )
    }
}