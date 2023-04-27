package com.example.feature_main.ui.screens.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.feature_main.R
import com.example.core.data.model.Result
import com.example.feature_main.ui.screens.utils.Utils
import com.example.feature_main.ui.theme.ComicAuthorList
import com.example.feature_main.ui.theme.ComicDescriptionList
import com.example.feature_main.ui.theme.ComicTitle
import com.example.feature_main.ui.theme.MarvelComicsTheme

@Composable
fun ComicBooksList(
    comicsList: List<Result>,
    modifier: Modifier = Modifier,
    isEndReached: Boolean = false,
    loadComics: () -> Unit = {},
    onComicClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(items = comicsList) { index, comic ->
            if (index == 0)
                Spacer(modifier = Modifier.height(10.dp))

            ComicItem(comic) {
                onComicClicked(index)
            }

            if (index >= comicsList.count() - 1 && !isEndReached) {
                loadComics()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    CircularProgressIndicator(color = Color.Red, modifier = Modifier.size(40.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicItem(comic: Result, onComicClicked: () -> Unit) {
    val context = LocalContext.current

    val imageUrl = if (comic.images.isNotEmpty()) {
        val extension: String = comic.images[0].extension
        val imagePath: String = comic.images[0].path
        "$imagePath.$extension"
    } else ""

    val description = comic.description ?: stringResource(R.string.no_description_available)
    val numAuthors: Int = comic.creators.available

    val authors = Utils.getAuthors(context, numAuthors, comic)

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
            ComicThumbnail(modifier = Modifier.weight(2f), imageUrl)
            TitleAndDescription(modifier = Modifier.weight(3f), comic.title, authors, description)
        }
    }
}


@Composable
fun TitleAndDescription(
    modifier: Modifier = Modifier,
    title: String,
    authors: String,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.ComicTitle
        )
        Text(
            text = authors,
            color = Color.LightGray,
            style = MaterialTheme.typography.ComicAuthorList,
            maxLines = 2,
            softWrap = true,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = description,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.ComicDescriptionList,
            maxLines = 3,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun ComicThumbnail(modifier: Modifier = Modifier, imageUrl: String) {
    SubcomposeAsyncImage(
        model = imageUrl.ifEmpty { R.drawable.placeholder },
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Red,
                    modifier = Modifier.scale(0.4f)
                )
            }
        },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxHeight()
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    bottomStart = 20.dp,
                    topEnd = 0.dp,
                    bottomEnd = 0.dp
                )
            )
    )
}

@Composable
fun FavouriteStar(starColor: Color, onClick: () -> Unit) {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Favorite, tint = starColor, contentDescription = null)
    }
}

@Preview
@Composable
fun TitleAndDescriptionPreview() {
    MarvelComicsTheme {
        var clicked by remember {
            mutableStateOf(false)
        }
        val starColor by animateColorAsState(if (clicked) Color.Yellow else Color.DarkGray)
        Card(
            modifier = Modifier.size(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                FavouriteStar(starColor, onClick = {clicked = !clicked})
                TitleAndDescription(
                    title = "Spider man",
                    authors = "dupa",
                    description = "In the galaxy far far away dupa dupa dupa dupa duap"
                )
            }
        }
    }
}