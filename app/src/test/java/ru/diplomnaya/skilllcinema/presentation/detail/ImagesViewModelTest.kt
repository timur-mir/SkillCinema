package ru.diplomnaya.skilllcinema.presentation.detail

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import ru.diplomnaya.skilllcinema.model.allRepository.ActorRepository
import ru.diplomnaya.skilllcinema.model.allRepository.ImagesRepository
import ru.diplomnaya.skilllcinema.presentation.actors.ActorViewModel
import ru.diplomnaya.skilllcinema.presentation.actors.MainCoroutineRule

class ImagesViewModelTest {
    val mockRepo = Mockito.mock(ImagesRepository::class.java)
    var viewModel: ImagesViewModel = ImagesViewModel(mockRepo)
    var actual =
        "https://avatars.mds.yandex.net/get-kinopoisk-image/6201401/d2b76eb0-c645-4574-b309-4cc099cfd582/orig"
    var actual2 =
        "https://avatars.mds.yandex.net/get-kinopoisk-image/6201401/d2b76eb0-c645-4574-b309-4cc099cfd582/origin"

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun useImagesRepository() {
        runTest {
            CoroutineScope(mainCoroutineRule.testDispatcher).launch {
                viewModel.getImages(4536580, "POSTER", 1)
                assertSame(viewModel.images.value?.items?.get(0)?.imageUrl ?: actual2, actual)
            }
        }
    }
}