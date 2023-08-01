package io.github.qixiaoo.crystallite.ui.screens.me

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.github.qixiaoo.crystallite.ui.common.Route


fun NavGraphBuilder.me() {
    composable(Route.Me.route) {
        Me()
    }
}
