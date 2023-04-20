package com.example.marvelcomics.ui.screens.details

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.marvelcomics.R
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.components.ComicTopAppBar
import com.example.marvelcomics.ui.screens.main.MainViewModel
import com.example.marvelcomics.ui.screens.utils.Utils
import com.example.marvelcomics.ui.theme.BottomSheetButtonColor
import com.example.marvelcomics.ui.theme.ComicTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    val title: String =
        comicsData?.title ?: stringResource(R.string.details_screen_no_title_available)
    val description =
        comicsData?.description ?: stringResource(R.string.details_screen_no_desc_available)
    val numAuthors: Int = comicsData?.creators?.available ?: 0

    val context = LocalContext.current

    val authors = Utils.getAuthorsWithoutWrittenBy( numAuthors, comicsData)
    val detailsUrl = comicsData?.urls?.get(0)?.url

    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState(0)

    val coroutineScope = rememberCoroutineScope()

    val uriHandler = LocalUriHandler.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ComicTopAppBar(
                title = "Details",
                icon = painterResource(id = R.drawable.ic_arrow),
                onNavigationIconClicked = {
                    navController.popBackStack()
                },
                textStyle = MaterialTheme.typography.ComicTitle
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
        floatingActionButton = {
            FindOutMoreFAB(
                modifier = Modifier.padding(horizontal = 16.dp),
                detailsUrl = detailsUrl,
                context = context,
                uriHandler = uriHandler
            )
        },

        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = sheetState
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val imageUrl = if (comicsData?.images?.isNotEmpty() == true) {
                val extension: String = comicsData.images[0].extension
                val imagePath: String = comicsData.images[0].path
                "$imagePath.$extension"
            } else ""

            AsyncImage(
                model = imageUrl.ifEmpty { R.drawable.placeholder },
                contentDescription = stringResource(id = R.string.details_screen_image_desc),
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
                        scrollState = scrollState,
                        sheetState = sheetState,
                        coroutineScope = coroutineScope,
                        paddingValues = paddingValues
                    )
                },
                sheetShape = RoundedCornerShape(25.dp),
                sheetPeekHeight = 250.dp,
                sheetBackgroundColor = MaterialTheme.colors.surface,
                backgroundColor = Color.Transparent
            ) {
            }
        }
    }
}

@Composable
fun FindOutMoreFAB(
    modifier: Modifier = Modifier,
    detailsUrl: String?,
    context: Context,
    uriHandler: UriHandler
) {
    CustomFAB(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.White
            ),
        onClick = {
            if (detailsUrl != null) {
                uriHandler.openUri(detailsUrl)
            } else {
                Toast.makeText(
                    context,
                    R.string.details_screen_no_details_available,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )
}


@Composable
fun CustomFAB(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
    ) {
        Text(
            text = stringResource(R.string.details_screen_fab_text),
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    title: String,
    authors: String,
    description: String,
    scrollState: ScrollState,
    sheetState: BottomSheetState,
    coroutineScope: CoroutineScope,
    paddingValues: PaddingValues
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 5.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = paddingValues.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    coroutineScope.launch {
                        if (sheetState.isCollapsed)
                            sheetState.expand()
                        else sheetState.collapse()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            BottomSheetButton(
                modifier = Modifier
                    .fillMaxWidth(0.20f)
                    .height(7.5.dp)
            ) {
                coroutineScope.launch {
                    if (sheetState.isCollapsed)
                        sheetState.expand()
                    else sheetState.collapse()
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, style = MaterialTheme.typography.h5)
            if (authors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = authors,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.body2
                )
            }
            if (description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(15.dp))
                HtmlText(
                    style = MaterialTheme.typography.body1,
                    text = description,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun BottomSheetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = BottomSheetButtonColor,
                shape = RoundedCornerShape(40.dp))
            .clickable(
                onClick = onClick
            )
    )
}

@Composable
private fun HtmlText(
    style: TextStyle,
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    lineSpacing: Float = 1.2f
) {
    val fontSize = style.fontSize.value
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                textSize = fontSize
                setTextColor(color.toArgb())
                setLineSpacing(1f, lineSpacing)
            }
        },
        modifier = modifier,
        update = {
            it.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}