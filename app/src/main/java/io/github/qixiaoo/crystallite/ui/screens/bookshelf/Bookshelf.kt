package io.github.qixiaoo.crystallite.ui.screens.bookshelf

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Bookshelf(
) {
    Text(
        "bookshelf", modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    )
}