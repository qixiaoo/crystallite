package io.github.qixiaoo.crystallite.ui.common

const val slugArg = "slug"
const val chapterHidArg = "hid"

const val HomePath = "home"
const val BookshelfPath = "bookshelf"
const val MePath = "me"
const val ComicPath = "comic/{$slugArg}"
const val ReaderPath = "reader/{$chapterHidArg}"
const val SearchPath = "search"

val APP_BAR_SCREEN_ROUTE_LIST = listOf(HomePath)
val BOTTOM_BAR_SCREEN_ROUTE_LIST = listOf(HomePath, BookshelfPath, MePath)

sealed class Route(val route: String) {
    object Home : Route(HomePath)
    object Bookshelf : Route(BookshelfPath)
    object Me : Route(MePath)
    object Comic : Route(ComicPath)
    object Reader : Route(ReaderPath)
    object Search : Route(SearchPath)
}
