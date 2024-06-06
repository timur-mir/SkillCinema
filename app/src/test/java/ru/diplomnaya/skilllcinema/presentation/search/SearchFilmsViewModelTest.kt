package ru.diplomnaya.skilllcinema.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.diplomnaya.skilllcinema.model.allRepository.ImagesRepository
import ru.diplomnaya.skilllcinema.model.allRepository.SearchFilmsRepository

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.runner.RunWith
import org.mockito.Mockito


class SearchFilmsViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: SearchFilmsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun searchFilms() = runTest {
        Mockito.`when`(repository.searchFilms("FILM", 1, 5, 1, "YEAR", 5, 10, 2023, 1998, "ab"))
            .thenReturn(
                emptyList()
            )
        val viewModel = SearchFilmsViewModel(repository)
        viewModel.searchFilms("FILM", 1, 5, 1, "YEAR", 5, 10, 2023, 1998, "ab")
        val result = viewModel.responsList.getOrAwaitValue()
        Assert.assertEquals(0, result.size)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}

