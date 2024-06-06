package ru.diplomnaya.skilllcinema.presentation.actors

import android.util.Log
import androidx.test.core.app.ActivityScenario.launch
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.runner.RunWith
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import ru.diplomnaya.skilllcinema.model.allRepository.ActorRepository
import ru.diplomnaya.skilllcinema.model.entities.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Rule
import org.mockito.Mockito
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import java.util.concurrent.CountDownLatch

class ActorViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    val mockRepo = Mockito.mock(ActorRepository::class.java)
    var viewModel: ActorViewModel = ActorViewModel(mockRepo)



    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun closeDb() {
        Dispatchers.resetMain()

    }

    @Test
    fun useActorRepository() {
        runTest {
            val job = launch(Dispatchers.IO) {
                viewModel.getActorFullInfo(1208251)
                Assert.assertEquals(viewModel.actorFullInfo.value?.personId ?: 1208251, 1208251)
            }
        }
    }
}

