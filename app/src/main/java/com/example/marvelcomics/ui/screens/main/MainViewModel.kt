package com.example.marvelcomics.ui.screens.main

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
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

    var currentPage: Int = 0
    var isEndReached: Boolean = false

    init {
        //getComics()
        testGetComicsWithPaging()
    }

    //new getComics fun
    fun testGetComicsWithPaging() {
        viewModelScope.launch {
            _comicsData.value = comicRepository.getComics(
                offset = currentPage * Constants.PAGE_SIZE
            )

            isEndReached = if (_comicsData.value.data != null) {
                currentPage * Constants.PAGE_SIZE >= _comicsData.value.data!!.data.total
            } else {
                Log.d("MainViewModel", "Is end reached: true")
                true
            }

            _comicsData.value.data?.data?.results?.let { newResults ->
                if (newResults.isNotEmpty()) {
                    Log.d("MainViewModel", "New results added")
                    comicsList.value = comicsList.value + newResults
                    currentPage++
                }
            }
        }
    }

    private fun getComics() {
        viewModelScope.launch(Dispatchers.Default) {
            _comicsData.value =
                comicRepository.getComics(offset = currentPage * Constants.PAGE_SIZE)
            currentPage++
        }
    }

    fun getComicsPaginated() {
        Log.d("MainViewModel", "getComicsPaginated")
        viewModelScope.launch(Dispatchers.Default) {
//            _comicsData.value = comicRepository.getComics(offset = currentPage * Constants.PAGE_SIZE)
//            if(_comicsData.value.data?.data?.results?.isNotEmpty() == true) {
//                val list = _comicsData.value.data?.data?.results
//                comicRepository.getComics(offset = currentPage * Constants.PAGE_SIZE).data?.data?.results?.let { newComics ->
//                    list?.addAll(newComics)
//                    list?.let {
//                        _comicsData.value.data?.data?.results = it
//                        _comicsData.emit(_comicsData.value)
//                    }
//                }
//            }else
//                _comicsData.value =comicRepository.getComics(offset = currentPage * Constants.PAGE_SIZE)

            comicRepository.getComics2(offset = currentPage * Constants.PAGE_SIZE)?.let { newData ->
                Log.d("MainViewModel", "Nowa lista: ${newData.data.results}")
                val newList = _comicsData.value
                newList.data?.data?.apply {
                    Log.d("MainViewModel", "size before: ${results.size}")
                    results.addAll(newData.data.results)
                    Log.d("MainViewModel", "size after: ${results.size}")
                    offset = newData.data.offset
                }
                _comicsData.value = newList
            }


//            _comicsData.value.comicRepository.getComics(offset = currentPage * Constants.PAGE_SIZE)
            isEndReached = if (_comicsData.value.data != null) {
                currentPage * Constants.PAGE_SIZE >= _comicsData.value.data!!.data.total
            } else {
                Log.d("MainViewModel", "Is end reached: true")

                true
            }
        }
        currentPage++


        Log.d("MainViewModel", "Current page: $currentPage")
        Log.d("MainViewModel", "Offset: ${currentPage * Constants.PAGE_SIZE}")
    }

    fun getComicByTitle(title: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _comicsDataByTitle.value = comicRepository.getComicsByTitle(title)
        }
    }
}