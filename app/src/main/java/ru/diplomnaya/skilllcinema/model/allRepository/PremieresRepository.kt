package ru.diplomnaya.skilllcinema.model.allRepository

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

import ru.diplomnaya.skilllcinema.model.Country
import ru.diplomnaya.skilllcinema.model.Genre
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.CollectionsFilmsDatabase
import ru.diplomnaya.skilllcinema.model.database.DataBase

import ru.diplomnaya.skilllcinema.model.database.PremieresEntity
import ru.diplomnaya.skilllcinema.model.database.WantToSeeEntity
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.network.retrofit

import kotlin.collections.ArrayList


class PremieresRepository {

    private val alreadyViewedDao = DataBase.INSTANCE!!.alreadyViewedDao()
    private val premieresDao = DataBase.INSTANCE!!.premieresDao()
    var moviesListCheckedByViewedFilmList: MutableList<Movie> = mutableListOf()
    var premieresList: MutableList<PremieresEntity> = mutableListOf()
    var mutableListMoviesCopy: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmList: List<AlreadyViewedEntity>
    private val repoIOScope = CoroutineScope(Dispatchers.IO)
    private var downloader: Job? = null
    suspend fun getPremieres(year: Int, mounth: String): List<Movie> {
        val responseListFilms = retrofit.premieres(year, mounth).items.toMutableList()

        if (responseListFilms.isNotEmpty()) {
            for (item in responseListFilms) {
                premieresList.add(
                    PremieresEntity(
                        item.kinopoiskId,
                        item.nameRu,
                        item.posterUrl,
                        item.posterUrlPreview,
                        item.genres?.joinToString(",") { it.genre.toString() }.toString(),
                        item.premiereRu,
                        item.countries?.joinToString(",") { it.country.toString() }.toString(),
                        item.rating.toString(),
                        item.viewed,
                        item.filmId
                    )
                )
            }
            delay(300)
            save(premieresList)
            mutableListMoviesCopy = responseListFilms
            loadViewedFilms()
            delay(500)
            for (item in responseListFilms) {
                for (itemDao in viewedFilmList) {
                    if (item.kinopoiskId == itemDao.kinopoiskId

                    ) {
                        moviesListCheckedByViewedFilmList.add(
                            Movie(
                                item.kinopoiskId,
                                item.nameRu,
                                item.posterUrl,
                                item.posterUrlPreview,
                                item.genres,
                                item.premiereRu,
                                item.countries,
                                item.rating,
                                true,
                                item.filmId
                            )
                        )

                    }
                }
            }

            delay(200)

            for (movie in mutableListMoviesCopy) {

                for (movieBase in moviesListCheckedByViewedFilmList) {

                    if (movie.kinopoiskId == movieBase.kinopoiskId) {

                        responseListFilms.set(mutableListMoviesCopy.indexOf(movie), movieBase)

                    }

                }

            }
            delay(100)
        }
        return responseListFilms
    }

    suspend fun getPremieresAtDao(): List<Movie> {
        val responseListFilms = mutableListOf<Movie>()
        getList()
        delay(2000)
        for (item in premieresList) {
            responseListFilms.add(
                Movie(
                    item.kinopoiskId!!,
                    item.nameRu,
                    item.posterUrl,
                    item.posterUrlPreview,
                    arrayListOf(Genre(item.genres)),
                    item.premiereRu,
                    arrayListOf(Country(item.countries)),
                    item.rating,
                    false,
                    item.filmId!!
                )
            )
        }

        mutableListMoviesCopy = responseListFilms
        loadViewedFilms()

        delay(500)

        for (item in responseListFilms) {
            for (itemDao in viewedFilmList) {
                if (item.kinopoiskId == itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmList.add(
                        Movie(
                            item.kinopoiskId,
                            item.nameRu,
                            item.posterUrl,
                            item.posterUrlPreview,
                            item.genres,
                            item.premiereRu,
                            item.countries,
                            item.rating,
                            true,
                            item.filmId
                        )
                    )

                }
            }
        }

        delay(200)

        for (movie in mutableListMoviesCopy) {

            for (movieBase in moviesListCheckedByViewedFilmList) {

                if (movie.kinopoiskId == movieBase.kinopoiskId) {

                    responseListFilms.set(mutableListMoviesCopy.indexOf(movie), movieBase)

                }

            }

        }

        delay(200)
        return responseListFilms as List<Movie>
    }


    private suspend fun loadViewedFilms() {
        downloader ?: repoIOScope.launch(Dispatchers.IO) {
            alreadyViewedDao.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNot", "${e.message}")
                }
                .collect { response ->
                    viewedFilmList = response
                    downloader?.cancel()
                    downloader = null
                }
        }

    }

    private suspend fun save(list: MutableList<PremieresEntity>) {

        downloader ?: repoIOScope.launch(Dispatchers.IO) {
            premieresDao.insertPremieres(list)
            downloader?.cancel()
            downloader = null
        }
    }

    private fun getList() {
        premieresList = premieresDao.getPremieres() as MutableList<PremieresEntity>
        Log.d("TAG6", "$premieresList")
    }
}


