package io.github.qixiaoo.crystallite.ui.screens.reader

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.qixiaoo.crystallite.ui.common.Route
import io.github.qixiaoo.crystallite.ui.common.chapterHidArg
import java.net.URLDecoder


private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()


internal class ReaderArgs(val chapterHid: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(
                savedStateHandle[chapterHidArg]
            ), URL_CHARACTER_ENCODING
        )
    )
}


fun NavGraphBuilder.reader() {
    composable(
        Route.Reader.route, arguments = listOf(navArgument(chapterHidArg) { type = NavType.StringType })
    ) {
        it.arguments?.getString(chapterHidArg)?.let { Reader() }
    }
}


fun NavController.navigateToReader(chapterHid: String) {
    this.navigate("reader/$chapterHid")
}