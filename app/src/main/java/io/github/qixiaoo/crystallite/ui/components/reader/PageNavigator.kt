package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme
import kotlin.math.roundToInt


@Composable
fun PageNavigator(
    modifier: Modifier = Modifier,
    current: Int,
    pageCount: Int,
    onPageChange: (Int) -> Unit,
    margin: PaddingValues = PaddingValues(),
    readingMode: ReadingMode = ReadingMode.LeftToRight,
) {
    val typography = MaterialTheme.typography
    val colorScheme = MaterialTheme.colorScheme

    val direction = when (readingMode) {
        ReadingMode.LeftToRight -> LayoutDirection.Ltr
        ReadingMode.RightToLeft -> LayoutDirection.Rtl
    }

    val steps = if (pageCount > 2) pageCount - 2 else 0
    val pageRange = 0f..(pageCount - 1).toFloat()

    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(margin)
                .background(
                    color = colorScheme.primaryContainer, shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 15.dp)
                .then(modifier)
        ) {
            Text(text = "1", color = colorScheme.onPrimary, style = typography.bodyMedium)
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
                color = colorScheme.onPrimary,
                style = typography.bodyMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PageNavigatorPreview() {
    CrystalliteTheme {
        var current by remember { mutableIntStateOf(0) }
        PageNavigator(current = current, pageCount = 30, onPageChange = { current = it })
    }
}