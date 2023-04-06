package com.example.marvelcomics.ui.screens.details

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.marvelcomics.data.model.Result
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
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
    // TODO :
    //   - change comicsData to comicsList (the new one)
    //   - maybe pass comicsList[index] to Details screen
    //   - delete viewModel from the arguments and use the comicsList

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

    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState(2)

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmStateChange = { it != ModalBottomSheetValue.Hidden },
    )

//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = modalSheetState
//    )

    val detailsUrl = comicsData?.urls?.get(0)?.url

    val context = LocalContext.current

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
//        floatingActionButton = {
//            FindOutMoreFAB(
//                modifier = Modifier
//                    .fillMaxWidth(0.7f)
//                    .height(60.dp),
//                onClick = {
//                    val detailsUrl =
//                        comicsData?.urls?.get(0)?.url
//                    if (detailsUrl != null) {
//                        uriHandler.openUri(detailsUrl)
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "No details link available",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                }
//            )
//        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetContent = {

                BottomSheetContent(
                    //modifier = Modifier.defaultMinSize(minHeight = 1.dp),
                    title = title,
                    authors = authors,
                    description = description,
                    detailsUrl = detailsUrl,
                    scrollState = scrollState
                )

            },
            sheetElevation = 0.dp,
            scrimColor = Color.Unspecified
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val extension: String? =
                    comicsData?.thumbnail?.extension
                val imagePath: String? =
                    comicsData?.thumbnail?.path

                val detailsImageUrl = "$imagePath/detail.$extension"

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
    scrollState: ScrollState
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        //.scrollable(state = scrollState, orientation = Orientation.Vertical),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = authors,
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)// was fillMaxWidth()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge
            )
//            Spacer(modifier = Modifier.height(60.dp))
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//            }
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
