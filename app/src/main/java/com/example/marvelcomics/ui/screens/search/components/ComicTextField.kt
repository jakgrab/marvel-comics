package com.example.marvelcomics.ui.screens.search.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComicTextField(
    modifier: Modifier = Modifier,
    placeholderText: String,
    onSearch: (String) -> Unit,
    hideKeyboard: Boolean,
    onFocusClear: () -> Unit,
) {
    val comicBookTitle = remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val validState = remember(comicBookTitle.value) {
        comicBookTitle.value.trim().isNotEmpty()
    }
    val errorState = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    MyTextField(
        modifier = modifier,
        valueState = comicBookTitle,
        errorState = errorState,
        placeholderText = placeholderText,
        onAction = KeyboardActions {
            if (!validState) {
                errorState.value = true
                return@KeyboardActions
            }
            onSearch(comicBookTitle.value.trim())
            comicBookTitle.value = ""
            keyboardController?.hide()
            focusManager.clearFocus()
            errorState.value = false
        }
    )

    if (hideKeyboard) {
        focusManager.clearFocus()
        onFocusClear()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    errorState: MutableState<Boolean>,
    placeholderText: String,
    leadingIcon: ImageVector = Icons.Rounded.Search,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        value = valueState.value,
        onValueChange = { title ->
            valueState.value = title
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 20.sp),
        placeholder = {
            Text(text = placeholderText, fontSize = 20.sp)
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Search Icon"
            )
        },
        isError = errorState.value,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
        colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            containerColor = MaterialTheme.colors.background
        )
    )
}