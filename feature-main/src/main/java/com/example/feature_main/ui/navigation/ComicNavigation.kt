package com.example.feature_main.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature_main.ui.screens.details.DetailsScreen
import com.example.feature_main.ui.screens.favourite.FavouriteScreen
import com.example.feature_main.ui.screens.main.MainScreen
import com.example.feature_main.ui.screens.main.MainViewModel
import com.example.feature_main.ui.screens.search.SearchScreen


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
            route = "$detailsRoute/{previousScreen}/{comicIndex}",
            arguments = listOf(
                navArgument(name = "comicIndex") {
                    type = NavType.IntType
                },
                navArgument(name = "previousScreen") {
                    type = NavType.IntType
                }
            )
        ) { navBack ->
            val index = navBack.arguments?.getInt("comicIndex")
            val previousScreen = navBack.arguments?.getInt("previousScreen")
            DetailsScreen(
                mainViewModel = mainViewModel,
                navController = navController,
                comicIndex = index,
                previousScreen = previousScreen
            )
        }

        composable(
            route = ComicScreens.SearchScreen.name
        ) { SearchScreen(mainViewModel, navController) }

        composable(route = ComicScreens.FavouriteScreen.name) {
            FavouriteScreen(mainViewModel = mainViewModel, navController = navController)
        }
    }
}