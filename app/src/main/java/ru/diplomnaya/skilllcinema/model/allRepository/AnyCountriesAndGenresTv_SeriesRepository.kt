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


class AnyCountriesAndGenresTv_SeriesRepository {
    private val alreadyViewedDaoForTV = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmTV : MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopyTV: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmListTV: List<AlreadyViewedEntity>
    private val repoIOScopeTV = CoroutineScope(Dispatchers.IO)
    private var downloaderTV: Job?=null
    suspend fun getDifferentTv_Series(country:Int,genre:Int,page:Int):List<Movie> {
        val responseListFilmsTV =  retrofit.anyCountriesAndGenresTv_Series(country,genre,page).items.toMutableList()
        mutableListMoviesCopyTV = responseListFilmsTV
        loadViewedFilms()
        delay(500)
        for (item in responseListFilmsTV) {
            for (itemDao in viewedFilmListTV) {
                if (item.kinopoiskId == itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmTV .add(
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

        for (movie in mutableListMoviesCopyTV) {

            for (movieBase in moviesListCheckedByViewedFilmTV ) {

                if (movie.kinopoiskId== movieBase.kinopoiskId) {

                    responseListFilmsTV.set(mutableListMoviesCopyTV.indexOf(movie), movieBase)

                }

            }

        }
        delay(100)
        Log.d("TV", "Есть фильмы,  ${responseListFilmsTV[0]}")
        return responseListFilmsTV
    }
    private suspend fun loadViewedFilms() {
        downloaderTV?:
        repoIOScopeTV.launch(Dispatchers.IO) {
            alreadyViewedDaoForTV.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNotTVError: ", "${e.message}")
                }
                .collect { response ->
                    viewedFilmListTV = response
//                    Log.d("DaoNotTV", "Есть фильмы,  ${viewedFilmListTV[1]}")
                    downloaderTV?.cancel()
                    downloaderTV=null
                }
        }

    }
}