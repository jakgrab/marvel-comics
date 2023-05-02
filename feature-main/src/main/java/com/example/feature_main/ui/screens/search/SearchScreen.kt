package com.example.feature_main.ui.screens.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.feature_main.R
import com.example.feature_main.ui.navigation.ComicScreens
import com.example.feature_main.ui.screens.components.ComicBooksList
import com.example.feature_main.ui.screens.components.ComicBottomAppBar
import com.example.feature_main.ui.screens.main.MainViewModel
import com.example.feature_main.ui.screens.search.components.ComicTextField
import com.example.feature_main.ui.screens.utils.Destinations
import com.example.feature_main.ui.theme.CancelTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(mainViewModel: MainViewModel, navController: NavController) {

    val comicsDataByTitle = mainViewModel.comicsDataByTitle.collectAsState()

    val comicsListByTitle = remember(comicsDataByTitle) {
        mainViewModel.comicsListByTitle
    }

    var showFoundComics by remember {
        mutableStateOf(false)
    }

    var searchingForComic by remember {
        mutableStateOf(false)
    }

    if (comicsDataByTitle.value.loading == true) {
        showFoundComics = false
    } else {
        searchingForComic = false
        showFoundComics = true
    }

    val isResultEmpty by remember(comicsDataByTitle.value.data) {
        mutableStateOf(comicsListByTitle.value.isEmpty())
    }

    var comicBookTitle by remember {
        mutableStateOf("")
    }

    var hideKeyboard by remember {
        mutableStateOf(false)
    }

    val inputValue = mainViewModel.searchInputValue

    var isInputEmpty by remember(inputValue.value) {
        mutableStateOf(inputValue.value.isEmpty())
    }

    var searchFieldWidth by remember(isInputEmpty) {
        mutableStateOf(1f)
    }

    searchFieldWidth = if (isInputEmpty) 1f else 0.7f

    val animateSearchFieldWidth by animateFloatAsState(targetValue = searchFieldWidth)

    val isKeyboardOpen by keyboardAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MarvelSearchField(
                modifier = if (scrollBehavior.state.contentOffset < -10) {
                    Modifier.shadow(
                        elevation = 20.dp, shape = RectangleShape
                    )
                } else Modifier,
                animateSearchFieldWidth = animateSearchFieldWidth,
                inputValue = inputValue.value,
                isInputEmpty = isInputEmpty,
                hideKeyboard = hideKeyboard,
                onValueChange = { searchInput ->
                    mainViewModel.setSearchInputValue(searchInput)
                },
                onSearch = { comicTitle ->
                    comicBookTitle = comicTitle
                    searchingForComic = true
                    mainViewModel.getComicByTitle(comicBookTitle)
                },
                onSearchAfterDelay = { delayedInput ->
                    searchingForComic = true
                    mainViewModel.getComicByTitle(delayedInput)
                },
                onFocusClear = {
                    hideKeyboard = false
                },
                onTextClicked = {
                    mainViewModel.clearSearchInputValue()
                    isInputEmpty = !isInputEmpty
                    hideKeyboard = true
                    mainViewModel.cancelSearch()
                }
            )
        },
        bottomBar = {
            if (!isKeyboardOpen)
                ComicBottomAppBar(
                    onHomeIconClicked = {
                        navController.navigate(ComicScreens.MainScreen.name)
                    },
                    onFavouriteIconClicked = {
                        navController.navigate(ComicScreens.FavouriteScreen.name)
                    },
                    searchSelected = true
                )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material.MaterialTheme.colors.background)
                .padding(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .imePadding()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    hideKeyboard = true
                },
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = showFoundComics &&
                        comicsListByTitle.value.isNotEmpty() &&
                        inputValue.value.isNotEmpty() &&
                        !searchingForComic,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    initialOffsetY = {
                        it / 2
                    }
                )
            ) {
                ComicBooksList(
                    comicsList = comicsListByTitle.value,
                    modifier = Modifier.weight(1f),
                    onComicClicked = { comicIndex ->
                        navController.navigate(
                            ComicScreens.DetailsScreen.name +
                                    "/${Destinations.SEARCH_SCREEN}/$comicIndex"
                        )
                    },
                    onFavouriteClicked = { index ->
                        comicsListByTitle.value[index].apply {
                            isFavourite = !isFavourite
                        }
                    }
                )
            }
        }

        if (searchingForComic) {
            Loading()
        }

        if (inputValue.value.isNotEmpty() && isResultEmpty && !searchingForComic) {
            Log.d("Search", "No results found")
            NoResultsFound()
        }

        if (inputValue.value.isEmpty() && !searchingForComic) {
            Log.d("Search", "Initial prompt")
            InitialPrompt()
        }
    }
}


@Composable
private fun MarvelSearchField(
    modifier: Modifier = Modifier,
    animateSearchFieldWidth: Float,
    inputValue: String,
    hideKeyboard: Boolean,
    isInputEmpty: Boolean,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onSearchAfterDelay: (String) -> Unit,
    onFocusClear: () -> Unit,
    onTextClicked: () -> Unit
) {

    Row(
        modifier = modifier
            .background(androidx.compose.material.MaterialTheme.colors.background)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ComicTextField(
            modifier = Modifier
                .fillMaxWidth(fraction = animateSearchFieldWidth),
            inputValue = inputValue,
            placeholderText = stringResource(R.string.search_field_hint),
            onValueChange = {
                onValueChange(it)
            },
            onSearch = onSearch,
            hideKeyboard = hideKeyboard,
            onFocusClear = onFocusClear,
            isHintVisible = isInputEmpty,
            onSearchAfterDelay = onSearchAfterDelay
        )

        SlideInClickableText(
            isInputEmpty = isInputEmpty,
            text = stringResource(id = R.string.cancel),
            onTextClicked = onTextClicked,
            textColor = CancelTextColor
        )
    }
}


@Composable
private fun SlideInClickableText(
    isInputEmpty: Boolean,
    modifier: Modifier = Modifier,
    text: String,
    onTextClicked: () -> Unit,
    textColor: Color
) {
    AnimatedVisibility(
        visible = !isInputEmpty,
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            ),
            initialOffsetX = { 50 }
        ),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            ),
            targetOffsetX = { -50 }
        )
    )
    {
        Text(
            text = text,
            modifier = modifier.clickable {
                onTextClicked()
            },
            fontSize = 20.sp,
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
private fun NoResultsFound() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_res_found),
                contentDescription = stringResource(R.string.search_screen_no_res_found_icon_desc),
                tint = Color.LightGray,
                modifier = Modifier.fillMaxWidth(0.3f)
            )
            Text(
                text = stringResource(R.string.no_results_found),
                textAlign = TextAlign.Center,
                color = androidx.compose.material.MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun Loading() {
    Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.Red)
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
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = stringResource(id = R.string.initial_prompt_icon_description),
                modifier = Modifier.size(100.dp),
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = stringResource(R.string.search_screen_initial_prompt),
                color = androidx.compose.material.MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center ,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}


@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}