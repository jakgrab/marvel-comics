package com.example.feature_main.ui.screens.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.feature_main.R

@Composable
fun ComicBottomAppBar(
    onHomeIconClicked: () -> Unit = {},
    onSearchIconClicked: () -> Unit = {},
    onFavouriteIconClicked: () -> Unit = {},
    homeSelected: Boolean = false,
    searchSelected: Boolean = false,
    favouriteSelected: Boolean = false
) {

    BottomAppBar(
        modifier = Modifier.advancedShadow(
            alpha = 0.1f,
            cornersRadius = 10.dp,
            shadowBlurRadius = 15.dp,
            offsetY = (-5).dp
        ),
        containerColor = MaterialTheme.colors.surface
    ) {
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
        NavigationBarItem(
            selected = favouriteSelected,
            onClick = { onFavouriteIconClicked() },
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier.size(25.dp),
                    contentDescription = "Favourites screen icon"
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

fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}