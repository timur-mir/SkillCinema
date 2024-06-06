package ru.diplomnaya.skilllcinema

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.diplomnaya.skilllcinema.model.Genre
import ru.diplomnaya.skilllcinema.model.database.CollectionsFilmsDatabase
import ru.diplomnaya.skilllcinema.model.database.PremieresDao
import ru.diplomnaya.skilllcinema.model.database.PremieresEntity
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class CollectionsFilmsDatabaseTest {
    private lateinit var db: CollectionsFilmsDatabase
    private lateinit var dao: PremieresDao
    private val testDispatcher = TestCoroutineDispatcher()
    private val filmInfo = PremieresEntity(
        4536580,
        "Баба Яга спасает мир",
        "https://kinopoiskapiunofficial.tech/images/posters/kp/4536580.jpg",
        "https://kinopoiskapiunofficial.tech/images/posters/kp_small/4536580.jpg",
        "фэнтези",
        "2023-08-03",
        "Россия",
        "7.3",
        true,
        4536580
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // val context= ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CollectionsFilmsDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        dao = db.premieresDao()
    }

    @After
    fun closeDb() {
        db.close()
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun insertAndGetPremieres() = runBlocking {
        dao.insertPremiere(filmInfo)
        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            dao.getPremiereById(4536580).collect { data ->
                assertThat(data, equalTo(filmInfo))
                latch.countDown()
            }
        }

        latch.await()
        job.cancel()
    }
}