package com.example.feature_main.ui.screens.main

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.constants.Constants
import com.example.core.data.model.Comics
import com.example.core.data.model.Result
import com.example.core.repository.comic_repository.ComicRepository
import com.example.core.repository.firebase_repository.FirebaseRepository
import com.example.core.sign_in.GoogleAuthUiClient
import com.example.core.wrapper.DataOrException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val comicRepository: ComicRepository,
    private val googleAuthUiClient: GoogleAuthUiClient,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _comicsData =
        MutableStateFlow<DataOrException<Comics, Boolean, Exception>>(
            DataOrException(loading = true)
        )

    private val _comicsDataByTitle =
        MutableStateFlow<DataOrException<Comics, Boolean, Exception>>(
            DataOrException(loading = true)
        )

    val comicsData = _comicsData.asStateFlow()
    val comicsDataByTitle = _comicsDataByTitle.asStateFlow()

    var comicsList: MutableState<List<Result>> = mutableStateOf(emptyList())
    var comicsListByTitle: MutableState<List<Result>> = mutableStateOf(emptyList())

    private var currentPage: Int = 0
    var isEndReached: Boolean = false

    private val _searchInputValue = mutableStateOf("")
    val searchInputValue: State<String> = _searchInputValue

    init {
        getComicsWithPaging()
    }

    fun getComicsWithPaging() {
        viewModelScope.launch {
            _comicsData.value = comicRepository.getComics(
                offset = currentPage * Constants.PAGE_SIZE
            )

            val maxComics: Int = _comicsData.value.data.let {
                it?.data?.total ?: 0
            }

            isEndReached = if (_comicsData.value.data != null) {
                currentPage * Constants.PAGE_SIZE >= maxComics
            } else {
                true
            }
            _comicsData.value.data?.data?.results?.let { newResults ->
                if (newResults.isNotEmpty()) {
                    comicsList.value = comicsList.value + newResults.mapIndexed{ index, it ->
                        if(index%3 == 0) it.isFavourite = true
                        it
                    }
                    currentPage++
                }
            }
        }
    }

    fun getComicByTitle(title: String) {
        viewModelScope.launch {
            _comicsDataByTitle.value = comicRepository.getComicsByTitle(title)

            _comicsDataByTitle.value.data?.data?.results?.let { newResults ->
                if (newResults.isNotEmpty()) {
                    comicsListByTitle.value = newResults.mapIndexed{ index, it ->
                        if(index%3 == 0) it.isFavourite = true
                        it
                    }
                }
            }
        }
    }

    fun cancelSearch() {
        _comicsDataByTitle.value.data = null
    }

    fun setSearchInputValue(value: String) {
        _searchInputValue.value = value
    }

    fun clearSearchInputValue() {
        _searchInputValue.value = ""
    }

    fun logOut(activity: Activity?) {
        Firebase.auth.signOut()
        googleAuthUiClient.signOut(
            onSuccess = {
            },
            onError = {
            }
        )

        activity?.finish()
    }
}