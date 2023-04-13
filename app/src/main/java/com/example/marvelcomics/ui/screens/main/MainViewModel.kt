package com.example.marvelcomics.ui.screens.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelcomics.data.constants.Constants
import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.data.repository.ComicRepository
import com.example.marvelcomics.data.wrapper.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val comicRepository: ComicRepository) :
    ViewModel() {

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

    private var currentPage: Int = 0
    var isEndReached: Boolean = false

    init {
        getComicsWithPaging()
    }

    fun getComicsWithPaging() {
        viewModelScope.launch {
            _comicsData.value = comicRepository.getComics(
                offset = currentPage * Constants.PAGE_SIZE
            )

            isEndReached = if (_comicsData.value.data != null) {
                currentPage * Constants.PAGE_SIZE >= _comicsData.value.data!!.data.total
            } else {
                true
            }
            _comicsData.value.data?.data?.results?.let { newResults ->
                if (newResults.isNotEmpty()) {
                    comicsList.value = comicsList.value + newResults
                    currentPage++
                }
            }
        }
    }

    fun getComicByTitle(title: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _comicsDataByTitle.value = comicRepository.getComicsByTitle(title)
        }
    }

    fun cancelSearch() { _comicsDataByTitle.value.data = null }
}