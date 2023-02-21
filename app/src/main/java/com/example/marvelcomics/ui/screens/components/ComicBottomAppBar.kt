package com.example.marvelcomics.ui.screens.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun ComicBottomAppBar(
    onHomeIconClicked: () -> Unit = {},
    onSearchIconClicked: () -> Unit = {},
    homeSelected: Boolean = false,
    searchSelected: Boolean = false
) {

    BottomAppBar(containerColor = MaterialTheme.colors.surface) {
        NavigationBarItem(
            selected = homeSelected,
            onClick = {
                onHomeIconClicked()
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "home screen icon"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red,
                unselectedIconColor = Color.LightGray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = searchSelected,
            onClick = {
                onSearchIconClicked()
            },
            icon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "search screen icon"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red,
                unselectedIconColor = Color.LightGray,
                indicatorColor = Color.Transparent
            )
        )
    }
}