package com.example.marvelcomics.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marvelcomics.ui.navigation.ComicScreens
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.components.ComicTopAppBar
import com.example.marvelcomics.ui.theme.MarvelComicsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

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
                    //navController.navigate(SearchScreen...)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = it.calculateTopPadding(), start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicItem() {
    val imageUrlTest =
        "http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge.jpg"
    val title = "Amazing Spider Max #255"
    val authors = "Your Mom, George W. Bush"
    val description =
        "Marvel celebrates Black History Month with this special one-shot" +
                " featuring the iconic heroes of Wakanda! Black Panther, Shuri, Okoye and more" +
                " star in all-new stories by an incredible lineup of both fan-favorite creators " +
                "and talent fresh to the Marvel Universe. Join them as they grow and expand the " +
                "inimitable world of Wakanda in these tales of myth, adventure, strife, and more! " +
                "Including the debut of the LAST Black Panther in a story set in Wakanda's future! " +
                "Marvel's Voices program is the first stop in getting deeper looks into the world" +
                " outside your window!"

    Card(
        onClick = {
            /* TODO navController.navigate(ComicScreens.DetailsScreen)
              *     or OnItemClicked() and then in LazyColumn navigate to details
             */
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 25.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Start) {
            ComicThumbnail(imageUrlTest)
            TitleAndDescription(title, authors, description)
        }
    }
}

@Composable
fun TitleAndDescription(title: String, authors: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Text(text = "written by $authors", color = Color.LightGray)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = description, softWrap = true, style = MaterialTheme.typography.bodyMedium)

    }
}

@Composable
fun ComicThumbnail(imageUrl: String) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        //placeholder = painterResource(id = ),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxHeight()
            .width(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(0.5.dp, color = Color.Red)
    )
}

@Preview
@Composable
fun ComicItemPreview() {
    MarvelComicsTheme {
        ComicItem()
    }
}