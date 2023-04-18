package com.example.marvelcomics.ui.screens.main

import com.example.marvelcomics.data.model.Comics
import com.example.marvelcomics.data.model.Data
import com.example.marvelcomics.data.model.Result
import com.example.marvelcomics.data.repository.ComicRepository
import com.example.marvelcomics.data.wrapper.DataOrException
import com.example.marvelcomics.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

//    @ExperimentalCoroutinesApi
//    private val dispatcher = StandardTestDispatcher()
//
//    @get:Rule
//    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel


    private var fakeResult =
        mockk<Result>(relaxed = true)

    private var fakeData = mockk<Data>(relaxed = true)

    private var fakeComics = mockk<Comics>(relaxed = true)

    private val fakeDataOrException =
        mockk<DataOrException<Comics, Boolean, Exception>>(relaxed = true)

    private val fakeRepository: ComicRepository = mockk {
        coEvery { getComicsByTitle(any()) } returns fakeDataOrException
    }

    @Before
    fun setup() {
//        Dispatchers.setMain(dispatcher)

        fakeResult = fakeResult.copy(title = "Spider-man", description = "description")
        fakeData = fakeData.copy(results = arrayListOf(fakeResult, fakeResult, fakeResult))
        fakeComics = fakeComics.copy(data = fakeData)

        fakeDataOrException.data = fakeComics

        fakeRepository.apply {
            coEvery {
                getComics(0)
            } returns DataOrException(data = fakeComics)

            coEvery {
                getComicsByTitle(any())
            } returns fakeDataOrException
        }
        viewModel = MainViewModel(comicRepository = fakeRepository)
    }

//    @ExperimentalCoroutinesApi
//    @After
//    fun clean() {
//        Dispatchers.resetMain()
//    }

    @Test
    fun `WHEN title starts with Spider RETURN Spider-man comics`() = runTest {
        viewModel.getComicByTitle(title = "Spider")
        advanceUntilIdle()
        val dataorexception = viewModel.comicsDataByTitle.value as DataOrException<Comics, Boolean, Exception>
        val comics: Comics? = dataorexception.data
        val data = comics?.data
        val results = data?.results
        val title = results?.get(0)?.title
        assertEquals(
            fakeResult.title,
            title
        )
    }
}

class FakeDataOrException<Comics, Boolean, E: Exception>(
    var data: Comics? = null,
    var loading: Boolean? = null,
    var exception: E? = null
)