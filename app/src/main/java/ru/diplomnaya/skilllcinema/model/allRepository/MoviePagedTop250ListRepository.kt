package ru.diplomnaya.skilllcinema.model.allRepository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.network.retrofit


class MoviePagedTop250ListRepository {
    private val alreadyViewedDaoTop250 = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmListTop250: MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopyTop250: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmListTop250: List<AlreadyViewedEntity>
    private val repoIOScopeTop250 = CoroutineScope(Dispatchers.IO)
    private var downloaderTop250: Job?=null
    suspend fun getTop250List(page:Int):List<Movie> {
        val responseListFilmsTop250=retrofit.top250List(page).films.toMutableList()
        mutableListMoviesCopyTop250 = responseListFilmsTop250
        loadViewedFilms()
        delay(500)
        for (item in responseListFilmsTop250) {
            for (itemDao in viewedFilmListTop250) {
                if (item.filmId == itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmListTop250.add(
                        Movie(
                            item.kinopoiskId,
                            item.nameRu,
                            item.posterUrl,
                            item.posterUrlPreview,
                            item.genres,
                            item.premiereRu,
                            item.countries,
                            item.rating,
                            viewed = true,
                            item.filmId
                        )
                    )

                }
            }
        }

        delay(200)

        for (movie in mutableListMoviesCopyTop250) {

            for (movieBase in moviesListCheckedByViewedFilmListTop250) {

                if (movie.nameRu == movieBase.nameRu) {

                    responseListFilmsTop250.set(mutableListMoviesCopyTop250.indexOf(movie), movieBase)

                }

            }

        }
        delay(100)
        return responseListFilmsTop250
    }
    private suspend fun loadViewedFilms() {
        downloaderTop250?:
        repoIOScopeTop250.launch(Dispatchers.IO) {
            alreadyViewedDaoTop250.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNotTop250Error:", "${e.message}")
                }
                .collect { response ->
                    viewedFilmListTop250 = response
                    downloaderTop250?.cancel()
                    downloaderTop250=null
                }
        }

    }
}