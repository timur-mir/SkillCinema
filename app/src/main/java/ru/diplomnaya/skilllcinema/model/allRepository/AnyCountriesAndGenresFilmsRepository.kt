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

class AnyCountriesAndGenresFilmsRepository {
    private val alreadyViewedDaoForCountriesGenres = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmListCountriesGenres : MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopyCountriesGenres: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmListCountriesGenres: List<AlreadyViewedEntity>
    private val repoIOScopeCountriesGenres = CoroutineScope(Dispatchers.IO)
    private var downloaderCountriesGenres: Job?=null
    suspend fun getDifferentRandomFilms(country:Int,genre:Int,page:Int):List<Movie> {

        val responseListFilmsCountriesGenres= retrofit.anyCountriesAndGenresFilms(country,genre,page).items.toMutableList()
        mutableListMoviesCopyCountriesGenres = responseListFilmsCountriesGenres
        loadViewedFilms()
        delay(500)
        for (item in responseListFilmsCountriesGenres) {
            for (itemDao in viewedFilmListCountriesGenres) {
                if (item.kinopoiskId == itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmListCountriesGenres .add(
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

        for (movie in mutableListMoviesCopyCountriesGenres) {

            for (movieBase in moviesListCheckedByViewedFilmListCountriesGenres ) {

                if (movie.kinopoiskId == movieBase.kinopoiskId) {

                    responseListFilmsCountriesGenres.set(mutableListMoviesCopyCountriesGenres.indexOf(movie), movieBase)

                }

            }

        }
        delay(100)
        Log.d("CG", "Есть фильмы,  ${responseListFilmsCountriesGenres[0]}")
        return responseListFilmsCountriesGenres
    }
    private suspend fun loadViewedFilms() {
        downloaderCountriesGenres?:
        repoIOScopeCountriesGenres.launch(Dispatchers.IO) {
            alreadyViewedDaoForCountriesGenres.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNotCountriesGenresError: ", "${e.message}")
                }
                .collect { response ->
                    viewedFilmListCountriesGenres = response
//                    Log.d("DaoNotCountriesGenres", "Есть фильмы,  ${viewedFilmListCountriesGenres[1]}")
                    downloaderCountriesGenres?.cancel()
                    downloaderCountriesGenres=null
                }
        }

    }

}