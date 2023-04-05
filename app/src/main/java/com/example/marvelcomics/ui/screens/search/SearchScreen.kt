package com.example.marvelcomics.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImportContacts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    var newComicsList = remember(comicsDataByTitle) {
        mutableStateOf(comicsDataByTitle.value.data!!.data.results)
    }

    val fromMainScreen = false

    var showFoundComics by remember {
        mutableStateOf(false)
    }

    var searchingForComic by remember {
        mutableStateOf(false)
    }

    if (comicsDataByTitle.value.loading == true) {
        showFoundComics = false
    } else if (comicsDataByTitle.value.data != null) {
        comicsList = comicsDataByTitle.value.data!!.data.results
        searchingForComic = false
        showFoundComics = true
    }

    val noResultsFound by remember(comicsDataByTitle.value) {
        val isDataNull = comicsDataByTitle.value.data == null

        val isResultListEmpty: Boolean? = when (isDataNull) {
            true -> null
            false -> comicsDataByTitle.value.data!!.data.results.isEmpty()
        }

        mutableStateOf(isResultListEmpty)
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
                .background(androidx.compose.material.MaterialTheme.colors.background)
                .padding(
                    top = it.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding()
                )
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
                placeholderText = "Search for a comic book",
                onSearch = { comicTitle ->
                    comicBookTitle = comicTitle
                    searchingForComic = true
                    mainViewModel.getComicByTitle(comicBookTitle)
                },
                hideKeyboard = hideKeyboard,
                onFocusClear = {
                    hideKeyboard = false
                }
            )

            Spacer(modifier = Modifier.height(50.dp))

            AnimatedVisibility(visible = showFoundComics) {
                ComicBooksList(comicsList = newComicsList.value) { comicIndex ->
                    navController.navigate(
                        ComicScreens.DetailsScreen.name + "/$fromMainScreen/$comicIndex"
                    )
                }
            }

            AnimatedVisibility(visible = (searchingForComic)) {
                Loading()
            }

            AnimatedVisibility(visible = noResultsFound == true) {
                NoResultsFound()
            }

            AnimatedVisibility(visible = noResultsFound == null) {
                InitialPrompt()
            }
        }
    }
}

@Composable
private fun NoResultsFound() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = "No results found",
            color = androidx.compose.material.MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun InitialPrompt() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.ImportContacts,
                contentDescription = "Comic book icon",
                modifier = Modifier.size(100.dp),
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Start typing to find particular comics",
                color = androidx.compose.material.MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}