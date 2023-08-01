package io.github.qixiaoo.crystallite.ui.screens.bookshelf

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.qixiaoo.crystallite.ui.common.Route


fun NavGraphBuilder.bookshelf() {
    composable(Route.Bookshelf.route) {
        Bookshelf()
    }
}
