package com.example.feature_main.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.feature_main.R

val Roboto = FontFamily(
    fonts = listOf(
        Font(R.font.roboto_medium, FontWeight.Medium),
        Font(R.font.roboto_bold, FontWeight.Bold),
        Font(R.font.roboto_light, FontWeight.Light),
        Font(R.font.roboto_regular, FontWeight.Normal),
        Font(R.font.roboto_thin, FontWeight.Thin),
    )
)
// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val Typography.HeaderComicList: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W700,
            fontSize = 36.sp,
            lineHeight = 18.sp
        )
    }

val Typography.ComicTitle: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W500,
            fontSize = 20.sp,
            lineHeight = 21.sp
        )
    }

val Typography.ComicAuthorList: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 18.sp
        )
    }

val Typography.ComicDescriptionList: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 18.sp
        )
    }
