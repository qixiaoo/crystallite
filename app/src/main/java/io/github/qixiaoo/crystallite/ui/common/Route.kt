package io.github.qixiaoo.crystallite.ui.common

const val HomePath = "home"
const val BookshelfPath = "bookshelf"
const val MePath = "me"

sealed class Route(val route: String) {
    object Home : Route(HomePath)
    object Bookshelf : Route(BookshelfPath)
    object Me : Route(MePath)
}
