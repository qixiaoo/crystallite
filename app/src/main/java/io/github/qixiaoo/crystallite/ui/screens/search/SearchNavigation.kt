package io.github.qixiaoo.crystallite.ui.screens.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.qixiaoo.crystallite.ui.common.Route


fun NavGraphBuilder.search(onComicClick: (slug: String) -> Unit, searchViewModel: SearchViewModel) {
    composable(Route.Search.route) {
        Search(onComicClick = onComicClick, searchViewModel = searchViewModel)
    }
}


fun NavController.navigateSearch() {
    this.navigate(Route.Search.route)
}