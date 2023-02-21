package com.example.marvelcomics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.marvelcomics.ui.screens.details.DetailsScreen
import com.example.marvelcomics.ui.screens.main.MainScreen
import com.example.marvelcomics.ui.screens.main.MainViewModel
import com.example.marvelcomics.ui.screens.search.SearchScreen


@Composable
fun ComicNavigation() {

    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()

    NavHost(
        navController = navController,
        startDestination = ComicScreens.MainScreen.name
    ) {
        composable(
            route = ComicScreens.MainScreen.name
        ) { MainScreen(mainViewModel, navController) }

        val detailsRoute = ComicScreens.DetailsScreen.name

        composable(
            route = "$detailsRoute/{fromMainScreen}/{comicIndex}",
            arguments = listOf(
                navArgument(name = "fromMainScreen") {
                    type = NavType.BoolType
                },
                navArgument(name = "comicIndex") {
                    type = NavType.IntType
                }
            )
        ) { navBack ->
            val index = navBack.arguments?.getInt("comicIndex")
            val fromMainScreen = navBack.arguments?.getBoolean("fromMainScreen")

            DetailsScreen(
                mainViewModel = mainViewModel,
                navController = navController,
                comicIndex = index,
                fromMainScreen = fromMainScreen
            )

        }

        composable(
            route = ComicScreens.SearchScreen.name
        ) { SearchScreen(mainViewModel, navController) }
    }
}