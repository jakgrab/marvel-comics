package com.example.feature_main.ui.screens.search.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.feature_main.ui.theme.InputTextColor
import com.example.feature_main.ui.theme.PlaceholderColor
import com.example.feature_main.ui.theme.SearchFieldBackgroundColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComicTextField(
    modifier: Modifier = Modifier,
    inputValue: String,
    placeholderText: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    hideKeyboard: Boolean,
    onFocusClear: () -> Unit,
    isHintVisible: Boolean,
    onSearchAfterDelay: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val validState = remember(inputValue) {
        inputValue.trim().isNotEmpty()
    }

    val errorState = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = inputValue) {
        delay(100)
        if (!validState) return@LaunchedEffect
        onSearchAfterDelay(inputValue)
    }

    MyTextField(
        modifier = modifier,
        inputValue = inputValue,
        errorState = errorState,
        placeholderText = placeholderText,
        onValueChange = { input ->
            onValueChange(input)
        },
        onAction = KeyboardActions {
            if (!validState) {
                errorState.value = true
                return@KeyboardActions
            }
            onSearch(inputValue.trim())
            keyboardController?.hide()
            focusManager.clearFocus()
            errorState.value = false
        },
        isHintVisible = isHintVisible,
    )

    if (hideKeyboard) {
        focusManager.clearFocus()
        onFocusClear()
        keyboardController?.hide()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    inputValue: String,
    errorState: MutableState<Boolean>,
    placeholderText: String,
    leadingIcon: ImageVector = Icons.Rounded.Search,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onValueChange: (String) -> Unit,
    onAction: KeyboardActions = KeyboardActions.Default,
    isHintVisible: Boolean,
) {
    TextField(
        value = inputValue,
        onValueChange = { title ->
            onValueChange(title)
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 20.sp),
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = 20.sp,
                color = PlaceholderColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = stringResource(R.string.comic_text_field_icon_desc),
                modifier = Modifier.size(40.dp),
                tint = if (isHintVisible) Color.LightGray else Color.Gray
            )
        },
        isError = errorState.value,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction,
        singleLine = true,
        maxLines = 1,
        shape = RoundedCornerShape(10.dp),
        colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
            textColor = InputTextColor,
            cursorColor = InputTextColor,
            containerColor = SearchFieldBackgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}