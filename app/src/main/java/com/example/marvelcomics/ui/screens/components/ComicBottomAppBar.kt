package com.example.marvelcomics.ui.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.marvelcomics.R

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
                    modifier = Modifier.size(40.dp),
                    contentDescription = stringResource(R.string.bottom_app_bar_home_icon_desc)
                )
            },

            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red,
                unselectedIconColor = Color.LightGray,
                indicatorColor = MaterialTheme.colors.surface
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
                    modifier = Modifier.size(40.dp),
                    contentDescription = stringResource(R.string.bottom_app_bar_search_icon_desc)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Red,
                unselectedIconColor = Color.LightGray,
                indicatorColor = MaterialTheme.colors.surface
            )
        )
    }
}