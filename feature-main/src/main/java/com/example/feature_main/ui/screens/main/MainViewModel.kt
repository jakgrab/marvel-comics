package com.example.feature_main.ui.screens.main

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.constants.Constants
import com.example.core.data.firestore_data.ComicsData
import com.example.core.data.firestore_data.UserComicsData
import com.example.core.data.model.Comics
import com.example.core.data.model.Result
import com.example.core.repository.comic_repository.ComicRepository
import com.example.core.repository.firebase_repository.FirebaseRepository
import com.example.core.sign_in.GoogleAuthUiClient
import com.example.core.wrapper.DataOrException
import com.example.feature_main.ui.screens.utils.Utils
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

    var favouritesList = mutableStateListOf<ComicsData>()
    private lateinit var favouriteComicsIdList: List<Int>

    private var currentPage: Int = 0
    var isEndReached: Boolean = false

    private val _searchInputValue = mutableStateOf("")
    val searchInputValue: State<String> = _searchInputValue

    private val _wasAddingFavouritesSuccessful = MutableStateFlow(false)
    private val _wasUpdatingFavouritesSuccessful = MutableStateFlow(false)
    private val _wasDeletingFavouritesSuccessful = MutableStateFlow(false)

    val wasAddingComicSuccessful = _wasAddingFavouritesSuccessful.asStateFlow()
    val wasUpdatingComicSuccessful = _wasUpdatingFavouritesSuccessful.asStateFlow()
    val wasDeletingFavouritesSuccessful = _wasDeletingFavouritesSuccessful.asStateFlow()


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

            getFavouriteComics(
                onSuccess = {
                    _comicsData.value.data?.data?.results?.let { newResults ->
                        if (newResults.isNotEmpty()) {
                            comicsList.value = comicsList.value + newResults

                            favouriteComicsIdList = favouritesList.map { it.comicId.toInt() }

                            comicsList.value.onEach { comic ->
                                comic.isFavourite = favouriteComicsIdList.contains(comic.id)
                            }
                            currentPage++
                        }
                    }
                },
                onFailure = {
                    _comicsData.value.data?.data?.results?.let { newResults ->
                        if (newResults.isNotEmpty()) {
                            comicsList.value = comicsList.value + newResults
                            favouriteComicsIdList = emptyList()
                            currentPage++
                        }
                    }
                }
            )
        }


    }

    fun getComicByTitle(title: String) {
        viewModelScope.launch {
            _comicsDataByTitle.value = comicRepository.getComicsByTitle(title)

            _comicsDataByTitle.value.data?.data?.results?.let { newResults ->
                if (newResults.isNotEmpty()) {
                    comicsListByTitle.value = newResults
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

    fun addComicToFavourites(comic: Result) {
        viewModelScope.launch {
            if (favouritesList.isNotEmpty() && !favouriteComicsIdList.contains(comic.id)) {
                updateFavouriteComics(comic)
            }
            else {
                addTheFirstComicToFavourites(comic)
            }
        }
    }

    private fun addTheFirstComicToFavourites(comic: Result) {
        val comicsData = toComicsData(comic)

        viewModelScope.launch {
            firebaseRepository.addUsersFirstFavouriteComic(comicsData).collect { result: Boolean ->
                _wasAddingFavouritesSuccessful.value = result
            }
        }
    }

    private fun updateFavouriteComics(comic: Result) {
        val comicsData = toComicsData(comic)
        favouritesList.add(comicsData)
        viewModelScope.launch {
            firebaseRepository.updateFavouriteComics(favouritesList)
                .collect { result: Boolean ->
                    _wasUpdatingFavouritesSuccessful.value = result
                }
        }
    }

    private fun getFavouriteComics(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            firebaseRepository.getUsersFavouriteComics(
                onSuccess = onSuccess,
                onFailure = onFailure
            ).collect { favouriteComics: UserComicsData? ->
                favouriteComics?.comicsList?.forEach {
                    if (!favouritesList.contains(it))
                        favouritesList.add(it)
                }
            }

        }
    }

    fun deleteFromFavourites(comic: Result) {
        val comicsData = toComicsData(comic)
        favouritesList.remove(comicsData)

        viewModelScope.launch {
            firebaseRepository.updateFavouriteComics(favouritesList).collect { result ->
                _wasDeletingFavouritesSuccessful.value = result
            }
        }
    }

    private fun toComicsData(comic: Result): ComicsData {
        return ComicsData(
            comicId = comic.id.toString(),
            title = comic.title,
            description = comic.description ?: "",
            authors = Utils.getAuthorsWithoutWrittenBy(comic.creators.available, comic),
            image = Utils.getImageUrl(comic),
            url = comic.urls[0].url
        )
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
