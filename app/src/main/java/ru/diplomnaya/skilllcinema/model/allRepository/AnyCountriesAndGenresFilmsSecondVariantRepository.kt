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


class AnyCountriesAndGenresFilmsSecondVariantRepository {
    private val alreadyViewedDaoForCountriesGenresSecond = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmListCountriesGenresSecond : MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopyCountriesGenresSecond: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmListCountriesGenresSecond: List<AlreadyViewedEntity>
    private val repoIOScopeCountriesGenresSecond = CoroutineScope(Dispatchers.IO)
    private var downloaderCountriesGenresSecond: Job?=null

    suspend fun getDifferentRandomFilmsSecondVariant(country:Int,genre:Int,page:Int):List<Movie> {

        val responseListFilmsCountriesGenresSecond =  retrofit.anyCountriesAndGenresFilmsSecondVariant(country,genre,page).items.toMutableList()
        mutableListMoviesCopyCountriesGenresSecond = responseListFilmsCountriesGenresSecond
        loadViewedFilms()
        delay(500)
        for (item in responseListFilmsCountriesGenresSecond) {
            for (itemDao in viewedFilmListCountriesGenresSecond) {
                if (item.kinopoiskId == itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmListCountriesGenresSecond .add(
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

        for (movie in mutableListMoviesCopyCountriesGenresSecond) {

            for (movieBase in moviesListCheckedByViewedFilmListCountriesGenresSecond ) {

                if (movie.kinopoiskId == movieBase.kinopoiskId) {

                    responseListFilmsCountriesGenresSecond.set(mutableListMoviesCopyCountriesGenresSecond.indexOf(movie), movieBase)

                }

            }

        }
        delay(100)
        Log.d("CGS", "Есть фильмы,  ${responseListFilmsCountriesGenresSecond[0]}")
        return responseListFilmsCountriesGenresSecond
    }
    private suspend fun loadViewedFilms() {
        downloaderCountriesGenresSecond?:
        repoIOScopeCountriesGenresSecond.launch(Dispatchers.IO) {
            alreadyViewedDaoForCountriesGenresSecond.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNotCountriesGenresSecondError: ", "${e.message}")
                }
                .collect { response ->
                    viewedFilmListCountriesGenresSecond = response
//                    Log.d("DaoNotCountriesGenresSecond", "Есть фильмы,  ${viewedFilmListCountriesGenresSecond[1]}")
                    downloaderCountriesGenresSecond?.cancel()
                    downloaderCountriesGenresSecond=null
                }
        }

    }

}