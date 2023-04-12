package com.example.marvelcomics.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicTopAppBar(
    modifier: Modifier = Modifier,
    isForMainScreen: Boolean = false,
    title: String = "Screen",
    titleFontSize: TextUnit = 20.sp,
    titleFontWeight: FontWeight = FontWeight.Normal,
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

    if (isForMainScreen) {
        TopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
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
            colors = topAppBarColors
        )
    } else {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
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
}