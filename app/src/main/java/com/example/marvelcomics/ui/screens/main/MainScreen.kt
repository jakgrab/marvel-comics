package com.example.marvelcomics.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.marvelcomics.R
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBooksList
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.components.ComicTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel, navController: NavController) {

    val comicsData = mainViewModel.comicsData.collectAsState()

    val comicsList = remember(comicsData) {
        mainViewModel.comicsList
    }

    var isDataLoading by remember {
        mutableStateOf(false)
    }

    if (comicsData.value.loading == true) {
        isDataLoading = true
    } else if (comicsData.value.data != null) {
        isDataLoading = false
    }

    val fromMainScreen = true

    val scaffoldState = rememberScaffoldState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = topAppBarState
    )


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scaffoldState = scaffoldState,
        topBar = {
            ComicTopAppBar(
                modifier = if (scrollBehavior.state.contentOffset < 0f) {
                    Modifier.shadow(
                        elevation = 20.dp,
                        shape = RectangleShape
                    )
                } else Modifier,
                isForMainScreen = true,
                title = stringResource(R.string.main_screen_top_app_bar_title),
                titleFontSize = 25.sp,
                titleFontWeight = FontWeight.Bold,
                scrollBehavior = scrollBehavior
            )
        },

        bottomBar = {
            ComicBottomAppBar(
                onSearchIconClicked = {
                    navController.navigate(ComicScreens.SearchScreen.name)
                }, homeSelected = true
            )
        },
    ) {
        if (isDataLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Red)
            }
        } else if (comicsList.value.isNotEmpty()) {
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
                ComicBooksList(comicsList = comicsList.value,
                    isEndReached = mainViewModel.isEndReached,
                    loadComics = {
                        mainViewModel.getComicsWithPaging()
                    }) { comicIndex ->
                    navController.navigate(
                        ComicScreens.DetailsScreen.name + "/$fromMainScreen/$comicIndex"
                    )
                }
            }
        }
    }
}
