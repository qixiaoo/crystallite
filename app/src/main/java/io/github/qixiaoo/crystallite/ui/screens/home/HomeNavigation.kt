package io.github.qixiaoo.crystallite.ui.screens.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.qixiaoo.crystallite.ui.common.Route


fun NavGraphBuilder.home(onComicClick: (slug: String) -> Unit) {
    composable(Route.Home.route) {
        Home(onComicClick = onComicClick)
    }
}
