package io.github.qixiaoo.crystallite.ui.screens.comic

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.qixiaoo.crystallite.ui.common.Route
import io.github.qixiaoo.crystallite.ui.common.slugArg
import java.net.URLDecoder


private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()


internal class ComicArgs(val slug: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(
                savedStateHandle[slugArg]
            ), URL_CHARACTER_ENCODING
        )
    )
}


fun NavGraphBuilder.comic() {
    composable(
        Route.Comic.route, arguments = listOf(navArgument(slugArg) { type = NavType.StringType })
    ) {
        it.arguments?.getString(slugArg)?.let { _ -> Comic() }
    }
}

fun NavController.navigateToComic(slug: String) {
    this.navigate("comic/$slug")
}
