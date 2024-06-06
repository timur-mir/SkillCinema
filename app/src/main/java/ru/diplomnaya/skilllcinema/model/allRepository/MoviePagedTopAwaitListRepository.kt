package ru.diplomnaya.skilllcinema.model.allRepository


import android.util.Log
import android.widget.Toast
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



class MoviePagedTopAwaitListRepository {
    private val alreadyViewedDaoTopAwait = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmListTopAwait: MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopyTopAwait: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmListTopAwait: List<AlreadyViewedEntity>
    private val repoIOScopeTopAwait = CoroutineScope(Dispatchers.IO)
    private var downloaderTopAwait: Job?=null
    suspend fun getTopAwaitList(page: Int): List<Movie> {
        val responseListFilmsTopAwait=retrofit.topAwaitList(page).films.toMutableList()
        mutableListMoviesCopyTopAwait = responseListFilmsTopAwait
        loadViewedFilms()
      delay(500)
        for (item in responseListFilmsTopAwait) {
            for (itemDao in viewedFilmListTopAwait) {
                if (item.filmId == itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmListTopAwait.add(
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

        for (movie in mutableListMoviesCopyTopAwait) {

            for (movieBase in moviesListCheckedByViewedFilmListTopAwait) {

                if (movie.nameRu == movieBase.nameRu) {

                    responseListFilmsTopAwait.set(mutableListMoviesCopyTopAwait.indexOf(movie), movieBase)

                }


            }

        }
        delay(100)
        return responseListFilmsTopAwait
    }
    private suspend fun loadViewedFilms() {
        downloaderTopAwait?:
        repoIOScopeTopAwait.launch(Dispatchers.IO) {
            alreadyViewedDaoTopAwait.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNotTopAwait", "${e.message}")
                }
                .collect { response ->
                    viewedFilmListTopAwait = response
                    downloaderTopAwait?.cancel()
                    downloaderTopAwait=null
                }
        }

    }
}