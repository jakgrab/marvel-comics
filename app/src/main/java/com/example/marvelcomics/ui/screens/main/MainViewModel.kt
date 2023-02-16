package com.example.marvelcomics.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.repository.ComicRepositoryImpl
import com.example.marvelcomics.data.wrapper.DataOrException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val comicRepository: ComicRepositoryImpl) :
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

    init {
        getComics()
    }

    fun getComics() {
        viewModelScope.launch(Dispatchers.Default) {
           _comicsData.value = comicRepository.getComics()
        }
    }

    fun getComicByTitle(title: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _comicsDataByTitle.value = comicRepository.getComicsByTitle(title)
        }
    }
}