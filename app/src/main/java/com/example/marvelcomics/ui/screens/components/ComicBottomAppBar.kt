package com.example.marvelcomics.ui.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                    painter = painterResource(R.drawable.ic_house),
                    modifier = Modifier.size(20.dp),
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
                    painter = painterResource(R.drawable.ic_search),
                    modifier = Modifier.size(20.dp),
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