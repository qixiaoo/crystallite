package io.github.qixiaoo.crystallite.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.qixiaoo.crystallite.R
import io.github.qixiaoo.crystallite.ui.common.Route
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme


class TabItem(
    val route: Route,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val text: String = "",
    var selected: Boolean = false,
    var disabled: Boolean = false
)

@Composable
fun TabBar(
    currRoute: String? = Route.Home.route,
    onNavigateTo: (route: Route) -> Unit = {},
) {
    val tabItems = listOf(
        TabItem(Route.Home, Icons.Outlined.Home, Icons.Filled.Home, stringResource(R.string.home)),
        TabItem(
            Route.Bookshelf,
            Icons.Outlined.Book,
            Icons.Filled.Book,
            stringResource(R.string.bookshelf)
        ),
        TabItem(
            Route.Me,
            Icons.Outlined.EmojiEmotions,
            Icons.Filled.EmojiEmotions,
            stringResource(R.string.me)
        ),
    )

    tabItems.forEachIndexed { index, item -> item.selected = currRoute == item.route.route }

    NavigationBar {
        tabItems.forEachIndexed { _, item ->
            NavigationBarItem(
                selected = item.selected,
                onClick = { onNavigateTo(item.route) },
                icon = {
                    Icon(
                        if (!item.selected) item.icon else item.selectedIcon,
                        contentDescription = item.text
                    )
                },
                enabled = !item.disabled,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CrystalliteTheme {
        TabBar()
    }
}