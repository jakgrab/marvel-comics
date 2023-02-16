package com.example.marvelcomics.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*

@Composable
fun ComicBottomAppBar(
    onHomeIconClicked: () -> Unit = {},
    onSearchIconClicked: () -> Unit = {}
) {

    var homeIconSelected by remember {
        mutableStateOf(false)
    }

    var searchIconSelected by remember {
        mutableStateOf(false)
    }

    BottomAppBar() {
        NavigationBarItem(
            selected = homeIconSelected,
            onClick = {
                homeIconSelected = true
                onHomeIconClicked()
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "home screen icon"
                )
            }
        )
        NavigationBarItem(
            selected = searchIconSelected,
            onClick = {
                searchIconSelected = true
                onSearchIconClicked()
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "search screen icon"
                )
            }
        )
    }
}