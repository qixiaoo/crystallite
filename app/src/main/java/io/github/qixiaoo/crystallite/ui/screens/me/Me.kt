package io.github.qixiaoo.crystallite.ui.screens.me

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun Me(
) {
    Text(
        "me", modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    )
}
