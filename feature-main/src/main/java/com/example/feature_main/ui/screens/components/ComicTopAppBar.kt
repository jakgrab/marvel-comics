package com.example.feature_main.ui.screens.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComicTopAppBar(
    modifier: Modifier = Modifier,
    isForMainScreen: Boolean = false,
    title: String = "Screen",
    textStyle: TextStyle,
    icon: Painter? = null,
    actionIcon: ImageVector? = null,
    showDropDownMenu: Boolean = false,
    colors: TopAppBarColors? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigationIconClicked: () -> Unit = {},
    onActionIconClicked: () -> Unit = {},
    onLogOutClicked: () -> Unit = {}
) {

    val defaultTopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colors.background
    )

    val topAppBarColors = colors ?: defaultTopAppBarColors

    var isDropDownMenuVisible by remember {
        mutableStateOf(false)
    }

    if (isForMainScreen) {
        TopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    style = textStyle,
                    color = MaterialTheme.colors.onBackground
                )
            },
            navigationIcon = {
                if (icon != null) {
                    IconButton(onClick = { onNavigationIconClicked.invoke() }) {
                        Icon(
                            painter = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            },
            actions = {
                if (actionIcon != null) {
                    IconButton(
                        onClick = {
                            onActionIconClicked()
                            if (showDropDownMenu) isDropDownMenuVisible = true
                        }
                    ) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                    if (showDropDownMenu)
                        Menu(
                            expanded = isDropDownMenuVisible,
                            onDismissRequest = {
                                isDropDownMenuVisible = false
                            },
                            onLogOutClicked = onLogOutClicked
                        )
                }
            },
            colors = topAppBarColors,
            scrollBehavior = scrollBehavior
        )
    } else {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    style = textStyle,
                    color = MaterialTheme.colors.onBackground
                )
            },
            navigationIcon = {
                if (icon != null) {
                    IconButton(onClick = { onNavigationIconClicked.invoke() }) {
                        Icon(
                            painter = icon,
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

@Composable
private fun Menu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onLogOutClicked: () -> Unit
) {
    DropdownMenu(
        modifier = Modifier.background(Color.Red),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { androidx.compose.material3.Text("Log out") },
            onClick = onLogOutClicked
        )
    }
}