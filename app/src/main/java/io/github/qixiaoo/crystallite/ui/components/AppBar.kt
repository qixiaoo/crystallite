package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onClickSearch: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val colorScheme = MaterialTheme.colorScheme

    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.primaryContainer,
            scrolledContainerColor = Color.Black
        ),
        actions = {
            IconButton(onClick = onClickSearch) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "search",
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier.then(modifier)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    CrystalliteTheme {
        AppBar()
    }
}