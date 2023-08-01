package io.github.qixiaoo.crystallite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.qixiaoo.crystallite.ui.common.Route
import io.github.qixiaoo.crystallite.ui.components.TabBar
import io.github.qixiaoo.crystallite.ui.screens.bookshelf.bookshelf
import io.github.qixiaoo.crystallite.ui.screens.home.home
import io.github.qixiaoo.crystallite.ui.screens.me.me
import io.github.qixiaoo.crystallite.ui.theme.CrystalliteTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Main() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currRoute = navBackStackEntry?.destination?.route

    CrystalliteTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                bottomBar = {
                    TabBar(currRoute) {
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
                    home()
                    bookshelf()
                    me()
                }
            }
        }
    }
}