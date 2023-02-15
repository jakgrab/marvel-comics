package com.example.marvelcomics.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marvelcomics.ui.screens.main.MainScreen
import com.example.marvelcomics.ui.screens.search.SearchScreen


@Composable
fun ComicNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ComicScreens.MainScreen.name
    ) {
        composable(
            route = ComicScreens.MainScreen.name
        ) { MainScreen() }

        composable(
            route = ComicScreens.SearchScreen.name
        ) { SearchScreen() }
    }
}