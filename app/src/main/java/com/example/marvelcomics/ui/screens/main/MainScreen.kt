package com.example.marvelcomics.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBooksList
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.components.ComicTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel, navController: NavController) {

    mainViewModel.getComics()

    val comicsData = mainViewModel.comicsData.collectAsState()

    lateinit var comicsList: List<Result>

    var isDataLoading by remember {
        mutableStateOf(false)
    }

    if (comicsData.value.loading == true) {
        isDataLoading = true
    } else if (comicsData.value.data != null) {
        isDataLoading = false
        comicsList = comicsData.value.data!!.data.results
    }

    val fromMainScreen = true

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ComicTopAppBar(
                title = "Marvel Comics"
            )
        },
        bottomBar = {
            ComicBottomAppBar(
                onSearchIconClicked = {
                    navController.navigate(ComicScreens.SearchScreen.name)
                },
                homeSelected = true
            )
        },
    ) {
        if (isDataLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (comicsList.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material.MaterialTheme.colors.background)
                    .padding(
                        top = it.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = it.calculateBottomPadding()
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ComicBooksList(comicsList) { comicIndex ->
                    navController.navigate(
                        ComicScreens.DetailsScreen.name + "/$fromMainScreen/$comicIndex"
                    )
                }
            }
        }
    }
}
