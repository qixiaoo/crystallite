package io.github.qixiaoo.crystallite.ui.components.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.data.model.ReadingMode
import io.github.qixiaoo.crystallite.ui.common.toResourceId
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


val readingModeList = listOf(ReadingMode.LeftToRight, ReadingMode.RightToLeft)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTopBar(
    modifier: Modifier = Modifier,
    title: String? = "",
    chapter: String? = "",
    chapterTitle: String? = "",
    readingMode: ReadingMode,
    onNavigateBack: () -> Unit = {},
    onReadingModeChange: (ReadingMode) -> Unit = {},
) {
    val typography = MaterialTheme.typography

    var readingModeExpanded by remember { mutableStateOf(false) }

    val chapterName = if (!chapter.isNullOrEmpty() && !chapterTitle.isNullOrEmpty()) {
        stringResource(id = R.string.chapter_name_title, chapter, chapterTitle)
    } else if (!chapter.isNullOrEmpty()) {
        stringResource(id = R.string.chapter_name, chapter)
    } else {
        ""
    }

    Surface(tonalElevation = 6.dp, modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                // back
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(start = 5.dp, end = 8.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "navigate back",
                    )
                }

                // title
                Column {
                    title?.let {
                        Text(
                            text = it,
                            style = typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = chapterName,
                        style = typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // reading mode
            Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
                PlainTooltipBox(tooltip = { Text(stringResource(R.string.reading_mode)) }) {
                    IconButton(
                        onClick = { readingModeExpanded = true },
                        modifier = Modifier
                            .tooltipAnchor()
                            .padding(horizontal = 5.dp)
                    ) {
                        Icon(
                            Icons.Outlined.SystemUpdate,
                            contentDescription = "reading mode",
                        )
                    }
                }

                DropdownMenu(
                    expanded = readingModeExpanded,
                    onDismissRequest = { readingModeExpanded = false }
                ) {
                    for (it in readingModeList) {
                        DropdownMenuItem(
                            text = { Text(stringResource(it.toResourceId())) },
                            onClick = { onReadingModeChange(it) },
                            trailingIcon = {
                                if (readingMode == it) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "reading mode checked"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PageTopBarPreview() {
    CrystalliteTheme {
        PageTopBar(
            title = "Hello world",
            chapter = "0.5",
            chapterTitle = "First chapter",
            readingMode = ReadingMode.LeftToRight
        )
    }
}