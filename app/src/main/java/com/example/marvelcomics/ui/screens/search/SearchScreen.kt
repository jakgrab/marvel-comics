package com.example.marvelcomics.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marvelcomics.ui.screens.components.ComicBottomAppBar
import com.example.marvelcomics.ui.screens.search.components.ComicTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {

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
                    //navController homescreen
                },
                onSearchIconClicked = {}
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding(), start = 16.dp, end = 16.dp)
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
                onSearch = { comicTitle ->
                    comicBookTitle = comicTitle
                    // viewmodel.search
                },
                hideKeyboard = hideKeyboard,
                onFocusClear = {
                    hideKeyboard = false
                }
            )

            // if viewModel.comics is not null show comics
        }
    }
}