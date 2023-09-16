package io.github.qixiaoo.crystallite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.qixiaoo.crystallite.ui.common.APP_BAR_SCREEN_ROUTE_LIST
import io.github.qixiaoo.crystallite.ui.common.BOTTOM_BAR_SCREEN_ROUTE_LIST
import io.github.qixiaoo.crystallite.ui.common.Route
import io.github.qixiaoo.crystallite.ui.components.AppBar
import io.github.qixiaoo.crystallite.ui.components.TabBar
import io.github.qixiaoo.crystallite.ui.screens.bookshelf.bookshelf
import io.github.qixiaoo.crystallite.ui.screens.comic.comic
import io.github.qixiaoo.crystallite.ui.screens.comic.navigateToComic
import io.github.qixiaoo.crystallite.ui.screens.home.home
import io.github.qixiaoo.crystallite.ui.screens.me.me
import io.github.qixiaoo.crystallite.ui.screens.reader.navigateToReader
import io.github.qixiaoo.crystallite.ui.screens.reader.reader
import io.github.qixiaoo.crystallite.ui.screens.search.SearchViewModel
import io.github.qixiaoo.crystallite.ui.screens.search.navigateSearch
import io.github.qixiaoo.crystallite.ui.screens.search.search
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme

@Composable
fun Main() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currRoute = navBackStackEntry?.destination?.route

    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }

    // `AppBar` and `Search` should share the same `SearchViewModel`
    val searchViewModel = hiltViewModel<SearchViewModel>(viewModelStoreOwner)
    val searchViewModelKeyword = searchViewModel.keyword.collectAsStateWithLifecycle()

    CrystalliteTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    if (!APP_BAR_SCREEN_ROUTE_LIST.contains(currRoute)) {
                        return@Scaffold
                    }

                    AppBar(
                        keyword = searchViewModelKeyword.value,
                        onKeywordChange = searchViewModel::setKeyword,
                        onClickBack = navController::popBackStack,
                        onClickSearch = {
                            if (currRoute.equals(Route.Search.route)) {
                                return@AppBar
                            }

                            navController.navigateSearch()
                        },
                    )
                },
                bottomBar = {
                    if (!BOTTOM_BAR_SCREEN_ROUTE_LIST.contains(currRoute)) {
                        return@Scaffold
                    }

                    TabBar(currRoute) {
                        // avoid navigating to the current tab
                        if (currRoute.equals(it.route)) {
                            return@TabBar
                        }

                        // clear back stack and navigate to target tab screen
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                },
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Route.Home.route,
                    modifier = Modifier.padding(it)
                ) {
                    home(onComicClick = navController::navigateToComic)
                    bookshelf()
                    me()
                    comic(onChapterClick = navController::navigateToReader)
                    reader(onNavigateBack = navController::popBackStack)
                    search(
                        onComicClick = navController::navigateToComic,
                        searchViewModel = searchViewModel
                    )
                }
            }
        }
    }
}