package com.example.marvelcomics.ui.screens.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.ui.screens.utils.Utils

@Composable
fun ComicBooksList(
    comicsList: ArrayList<Result>,
    isEndReached: Boolean = false,
    loadComics: () -> Unit = {},
    onComicClicked: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        itemsIndexed(items = comicsList) { index, comic ->
            Log.d("MainViewModel", "List size: ${comicsList.count()}" +
                    " index: $index")
            if (index >= comicsList.count() - 1) {
                loadComics()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                ) {
                    CircularProgressIndicator(color = Color.Red, modifier = Modifier.size(40.dp))
                }
            }

            ComicItem(comic) {
                onComicClicked(index)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicItem(comic: Result, onComicClicked: () -> Unit) {

    val extension: String = comic.thumbnail.extension
    val imagePath: String = comic.thumbnail.path

    val imageUrl = "$imagePath/portrait_xlarge.$extension"

    val description = comic.description ?: "No description available"
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.surface
        ),
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
        Text(
            text = title,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6
        )
        Text(text = "written by $authors", color = Color.LightGray)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = description,
            color = MaterialTheme.colors.onBackground,
            softWrap = true,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun ComicThumbnail(imageUrl: String) {
    SubcomposeAsyncImage(
        model = imageUrl,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.4f)
                )
            }
        },
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
    )
}