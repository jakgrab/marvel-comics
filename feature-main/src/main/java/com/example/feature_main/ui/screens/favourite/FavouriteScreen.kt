package com.example.feature_main.ui.screens.favourite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.feature_main.R
import com.example.feature_main.ui.navigation.ComicScreens
import com.example.feature_main.ui.screens.components.ComicBottomAppBar
import com.example.feature_main.ui.screens.components.ComicTopAppBar
import com.example.feature_main.ui.screens.favourite.components.FavouriteComicsList
import com.example.feature_main.ui.screens.main.MainViewModel
import com.example.feature_main.ui.theme.HeaderComicList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(mainViewModel: MainViewModel, navController: NavController) {

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = topAppBarState
    )
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        topBar = {
            ComicTopAppBar(
                modifier = if (scrollBehavior.state.contentOffset < -10f) {
                    Modifier.shadow(
                        elevation = 20.dp,
                        shape = RectangleShape
                    )
                } else Modifier,
                title = stringResource(R.string.main_screen_top_app_bar_title),
                textStyle = MaterialTheme.typography.HeaderComicList,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            ComicBottomAppBar(
                onHomeIconClicked = {
                    navController.navigate(ComicScreens.MainScreen.name)
                },
                onSearchIconClicked = {
                    navController.navigate(ComicScreens.SearchScreen.name)
                },
                favouriteSelected = true
            )
        }
    ) { paddingValues ->
        if (mainViewModel.favouritesList.isNotEmpty()) {
            FavouriteComicsList()
        } else {
            NoFavouritesPrompt(paddingValues)
        }
    }
}

@Composable
private fun NoFavouritesPrompt(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp,
        ),
        contentAlignment = Alignment.Center
    ) { Text("No comic books added to favourites :(") }
}
