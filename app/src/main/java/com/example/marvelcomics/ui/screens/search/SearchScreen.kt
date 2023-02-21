package com.example.marvelcomics.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBooksList
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.main.MainViewModel
import com.example.marvelcomics.ui.screens.search.components.ComicTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(mainViewModel: MainViewModel, navController: NavController) {

    val comicsDataByTitle = mainViewModel.comicsDataByTitle.collectAsState()

    lateinit var comicsList: List<Result>

    val fromMainScreen: Boolean? = false

    var isDataLoading by remember {
        mutableStateOf(false)
    }
    var showFoundComics by remember {
        mutableStateOf(false)
    }

    if (comicsDataByTitle.value.loading == true) {
        isDataLoading = true
        showFoundComics = false
    } else if (comicsDataByTitle.value.data != null) {
        isDataLoading = false
        comicsList = comicsDataByTitle.value.data!!.data.results
        showFoundComics = true
    }

    var comicBookTitle by remember {
        mutableStateOf("")
    }

    var hideKeyboard by remember {
        mutableStateOf(false)
    }

    Scaffold(
        bottomBar = {
            ComicBottomAppBar(
                onHomeIconClicked = {
                    navController.navigate(ComicScreens.MainScreen.name)
                },
                searchSelected = true
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding(), start = 16.dp, end = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    hideKeyboard = true
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ComicTextField(
                modifier = Modifier.fillMaxWidth(),
                onSearch = { comicTitle ->
                    comicBookTitle = comicTitle
                    mainViewModel.getComicByTitle(comicBookTitle)
                },
                hideKeyboard = hideKeyboard,
                onFocusClear = {
                    hideKeyboard = false
                }
            )

            Spacer(modifier = Modifier.height(50.dp))

            AnimatedVisibility(visible = showFoundComics) {
                ComicBooksList(comicsList = comicsList) { comicIndex ->
                    navController.navigate(
                        ComicScreens.DetailsScreen.name + "/$fromMainScreen/$comicIndex"
                    )
                }
            }
            AnimatedVisibility(visible = (!showFoundComics && isDataLoading)) {
                Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}