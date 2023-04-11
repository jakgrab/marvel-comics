package com.example.marvelcomics.ui.screens.details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.components.ComicTopAppBar
import com.example.marvelcomics.ui.screens.main.MainViewModel
import com.example.marvelcomics.ui.screens.utils.Utils

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    fromMainScreen: Boolean?,
    comicIndex: Int?
) {
    val comicsData: Result? = if (fromMainScreen == true) {
        mainViewModel.comicsList.value[comicIndex!!]
    } else {
        mainViewModel.comicsDataByTitle.collectAsState().value.data?.data?.results?.get(comicIndex!!)
    }

    val title: String = comicsData?.title ?: "No title available"
    val description =
        comicsData?.description ?: "Description not available"
    val numAuthors: Int = comicsData?.creators?.available ?: 0

    val authors = Utils.getAuthors(numAuthors, comicsData)
    val detailsUrl = comicsData?.urls?.get(0)?.url

    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState(0)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ComicTopAppBar(
                title = "Details",
                icon = Icons.Rounded.ArrowBackIos,
                onNavigationIconClicked = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            ComicBottomAppBar(
                onHomeIconClicked = {
                    navController.navigate(ComicScreens.MainScreen.name)
                },
                onSearchIconClicked = {
                    navController.navigate(ComicScreens.SearchScreen.name)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = sheetState
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {

            val extension: String? =
                comicsData?.thumbnail?.extension
            val imagePath: String? =
                comicsData?.thumbnail?.path

            val detailsImageUrl = "$imagePath/detail.$extension"

            AsyncImage(
                model = detailsImageUrl,
                contentDescription = "Comic poster",
                modifier = Modifier.fillMaxHeight(),
                alignment = Alignment.TopCenter
            )
            BottomSheetScaffold(
                scaffoldState = bottomSheetScaffoldState,
                modifier = Modifier.padding(top = 200.dp),
                sheetContent = {
                    BottomSheetContent(
                        title = title,
                        authors = authors,
                        description = description,
                        detailsUrl = detailsUrl,
                        scrollState = scrollState,
                        paddingValues = paddingValues
                    )
                },
                sheetShape = RoundedCornerShape(25.dp),
                sheetPeekHeight = 200.dp,
                sheetBackgroundColor = MaterialTheme.colors.surface,
                backgroundColor = Color.Transparent
            ) {
            }
        }
    }
}

@Composable
fun FindOutMoreFAB(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
    ) {
        Text(text = "Find out more", color = Color.White)
    }
}

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    authors: String,
    description: String,
    detailsUrl: String?,
    scrollState: ScrollState,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 20.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(20.dp))

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (authors.isNotEmpty())
                Text(
                    text = authors,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.body2
                )
            if (description.isNotEmpty())
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1
                )

            Box(modifier = Modifier.padding(bottom = 20.dp)) {
                FindOutMoreFAB(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(60.dp),
                    onClick = {
                        if (detailsUrl != null) {
                            uriHandler.openUri(detailsUrl)
                        } else {
                            Toast.makeText(
                                context,
                                "No details link available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }
        }
    }
}

