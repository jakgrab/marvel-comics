package com.example.marvelcomics.ui.screens.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.ui.screens.utils.Utils

@Composable
fun ComicBooksList(
    comicsList: List<Result>,
    onComicClicked: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        itemsIndexed(items = comicsList) { index, comic ->
            ComicItem(comic) {
                onComicClicked(index)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicItem(comic: Result, onComicClicked: () -> Unit) {
//    val imageUrlTest =
//        "http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge.jpg"
//    val title = "Amazing Spider Max #255"
    //val authors = "Your Mom, George W. Bush"
//    val description =
//        "Marvel celebrates Black History Month with this special one-shot" +
//                " featuring the iconic heroes of Wakanda! Black Panther, Shuri, Okoye and more" +
//                " star in all-new stories by an incredible lineup of both fan-favorite creators " +
//                "and talent fresh to the Marvel Universe. Join them as they grow and expand the " +
//                "inimitable world of Wakanda in these tales of myth, adventure, strife, and more! " +
//                "Including the debut of the LAST Black Panther in a story set in Wakanda's future! " +
//                "Marvel's Voices program is the first stop in getting deeper looks into the world" +
//                " outside your window!"

    val extension: String = comic.thumbnail.extension
    val imagePath: String = comic.thumbnail.path

    val imageUrl = "$imagePath/portrait_xlarge.$extension"
    Log.d("IMG", "Image url: $imageUrl")

    val description = comic.description ?: ""
    val numAuthors: Int = comic.creators.available

    val utils = Utils()
    val authors = utils.getAuthors(numAuthors, comic)


    Card(
        onClick = {
            onComicClicked()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp, pressedElevation = 25.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Start) {
            ComicThumbnail(imageUrl)
            TitleAndDescription(comic.title, authors, description)
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
    SubcomposeAsyncImage(
        model = imageUrl,
        loading = {
            CircularProgressIndicator()
        },
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxHeight()
            .width(150.dp)
            .clip(RoundedCornerShape(20.dp))
    )
}