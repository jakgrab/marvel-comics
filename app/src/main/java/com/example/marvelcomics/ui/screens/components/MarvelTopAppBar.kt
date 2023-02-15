package com.example.marvelcomics.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarvelTopAppBar(
    title: String = "Screen",
    icon: ImageVector? = null,
    actionIcon: ImageVector? = null,
    colors: TopAppBarColors? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigationIconClicked: () -> Unit = {},
    onActionIconClicked: () -> Unit = {}
) {

    val defaultTopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colors.background
    )

    val topAppBarColors = colors ?: defaultTopAppBarColors

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                color = MaterialTheme.colors.onBackground
            )
        },
        navigationIcon = {
            if (icon != null) {
                IconButton(onClick = { onNavigationIconClicked.invoke() }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            } else {
                Box {}
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = { onActionIconClicked.invoke() }) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground
                    )
                }
            } else {
                Box {}
            }
        },
        colors = topAppBarColors,
        scrollBehavior = scrollBehavior
    )
}