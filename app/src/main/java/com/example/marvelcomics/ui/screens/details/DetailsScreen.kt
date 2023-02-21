package com.example.marvelcomics.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.components.ComicTopAppBar
import com.example.marvelcomics.ui.screens.main.MainViewModel
import com.example.marvelcomics.ui.screens.utils.Utils
import com.example.marvelcomics.ui.theme.MarvelComicsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DetailsScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    comicIndex: Int?
) {
    val comicsData = mainViewModel.comicsData.collectAsState()

    val title: String = comicsData.value.data!!.data.results[comicIndex!!].title
    val description =
        comicsData.value.data!!.data.results[comicIndex].description ?: "Description not available"
    val numAuthors: Int = comicsData.value.data!!.data.results[comicIndex].creators.available

    val utils = Utils()
    val authors = utils.getAuthors(numAuthors, comicsData.value.data!!.data.results[comicIndex])

    val scaffoldState = rememberScaffoldState()

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmStateChange = { it != ModalBottomSheetValue.Hidden },
    )

    val uriHandler = LocalUriHandler.current


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
                onSearchIconClicked = {
                    navController.navigate(ComicScreens.SearchScreen.name)
                }
            )
        },
        floatingActionButton = {
            FindOutMoreFAB(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                onClick = {
                    val detailsUrl =
                        comicsData.value.data!!.data.results[comicIndex].urls[0].url
                    uriHandler.openUri(detailsUrl)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {
                BottomSheetContent(title, authors, description)
            },
            sheetElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //if (comicsData.value.data != null && comicIndex != null) {
                val extension: String =
                    comicsData.value.data!!.data.results[comicIndex].thumbnail.extension
                val imagePath: String =
                    comicsData.value.data!!.data.results[comicIndex].thumbnail.path

                val detailsImageUrl = "$imagePath/detail.$extension"
                //}
                AsyncImage(
                    model = detailsImageUrl,
                    contentDescription = "Comic poster",
                    modifier = Modifier.fillMaxWidth()
                )
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
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
    ) {
        Text(text = "Find out more", color = Color.White)
    }
}

@Composable
fun BottomSheetContent(
    title: String,
    authors: String,
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = authors, color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview
@Composable
fun DetailsPReview() {
    MarvelComicsTheme {
//        DetailsScreen(mainViewModel, navController)
    }
}