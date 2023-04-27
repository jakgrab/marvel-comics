package com.example.feature_main.ui.screens.main
import com.example.core.data.model.Characters
import com.example.core.data.model.Comics
import com.example.core.data.model.Creator
import com.example.core.data.model.Creators
import com.example.core.data.model.Data
import com.example.core.data.model.Events
import com.example.core.data.model.Item
import com.example.core.data.model.Result
import com.example.core.data.model.Series
import com.example.core.data.model.Stories
import com.example.core.data.model.Thumbnail
import com.example.core.repository.comic_repository.ComicRepository
import com.example.core.wrapper.DataOrException
import com.example.feature_main.util.MainCoroutineRule
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

    private lateinit var viewModel: MainViewModel

    private var fakeResult = getResult()

    private var fakeData = getData(results = arrayListOf(fakeResult, fakeResult))

    private var fakeComics = getComics(data = fakeData)

    private val fakeDataOrException = getDataOrException(comics = fakeComics)

    private val fakeRepository: ComicRepository = mockk()


    @Before
    fun setup() {
        testSetup()
    }

    @Test
    fun `When title starts with Spider return Spider-man comics`() = runTest {
        viewModel.getComicByTitle(title = "Spider")
        advanceUntilIdle()
        val dataOrException = viewModel.comicsDataByTitle.value
        val comics: Comics? = dataOrException.data
        val data = comics?.data
        val results = data?.results
        val title = results?.get(0)?.title
        assertEquals(
            fakeResult.title,
            title
        )
    }

    @Test
    fun `When getting comics with paging ensure that comicsList is not empty`() = runTest {
        viewModel.getComicsWithPaging()
        advanceUntilIdle()
        assert(
            viewModel.comicsList.value.isNotEmpty()
        )
    }

    private fun getDataOrException(comics: Comics): DataOrException<Comics, Boolean, Exception> {
        return DataOrException(data = comics)
    }

    private fun getComics(data: Data): Comics {
        return Comics(
            code = 1,
            copyright = ",",
            data = data,
            ""
        )
    }

    private fun getData(results: ArrayList<Result>): Data {
        return Data(1, 1, 1, results, 1)
    }

    private fun getResult(): Result {
        return Result(
            characters = Characters(
                0,
                "",
                listOf(Item("", "")),
                1
            ),
            collectedIssues = listOf(),
            collections = listOf(),
            creators = Creators(
                1,
                items = listOf(Creator("", "", "")),
                1
            ),
            dates = listOf(),
            description = "fakeDescription",
            digitalId = 1,
            events = Events(1, "", listOf(), 1),
            format = "",
            images = listOf(),
            issueNumber = 1,
            modified = "",
            pageCount = 1,
            prices = listOf(),
            resourceURI = "",
            series = Series("", ""),
            stories = Stories(1, "", listOf(), 1),
            textObjects = listOf(),
            thumbnail = Thumbnail("", ""),
            title = "fakeTitle",
            urls = listOf(),
            "",
            listOf()
        )
    }

    private fun testSetup() {
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

//        val googleAuthUiClient = GoogleAuthUiClient()
//        viewModel = MainViewModel(comicRepository = fakeRepository)
    }
}
