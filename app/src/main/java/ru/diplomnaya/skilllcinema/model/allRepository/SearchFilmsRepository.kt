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
import ru.diplomnaya.skilllcinema.model.entities.AllCountriesGenresID
import ru.diplomnaya.skilllcinema.model.network.retrofit


class SearchFilmsRepository {
    private val alreadyViewedDaoSearch = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmListForSearchDif: MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopyForSearchDiff: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmListForSearchDiff: List<AlreadyViewedEntity>
    private val repoIOScopeSearch = CoroutineScope(Dispatchers.IO)
    private var downloaderForSearchDiff: Job?=null

    suspend fun searchFilms(
        type: String, countries: Int, genre: Int, page: Int,sort_by:String, ratingFrom: Int, ratingTo: Int,
        yearTo: Int, yearFrom: Int, keyword: String
    ): List<Movie> {
       var  responseListFilmsDiffSearchViewed =retrofit.searchFilms(
           type, countries, genre, page, sort_by,ratingFrom, ratingTo,
           yearFrom, yearTo, keyword
       ).items.toMutableList()
        mutableListMoviesCopyForSearchDiff = responseListFilmsDiffSearchViewed
        loadViewedFilms()
        delay(500)
        for (item in responseListFilmsDiffSearchViewed) {
            for (itemDao in viewedFilmListForSearchDiff) {
                if (item.kinopoiskId== itemDao.kinopoiskId

                ) {
                    moviesListCheckedByViewedFilmListForSearchDif.add(
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

        for (movie in mutableListMoviesCopyForSearchDiff) {

            for (movieBase in moviesListCheckedByViewedFilmListForSearchDif) {

                if (movie.kinopoiskId== movieBase.kinopoiskId) {

                    responseListFilmsDiffSearchViewed.set(mutableListMoviesCopyForSearchDiff.indexOf(movie), movieBase)

                }

            }

        }
        delay(100)
        return responseListFilmsDiffSearchViewed

    }
    private suspend fun loadViewedFilms() {
     repoIOScopeSearch.launch(Dispatchers.IO) {
            alreadyViewedDaoSearch.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNot", "${e.message}")
                }
                .collect { response ->
                    viewedFilmListForSearchDiff = response

                }
        }

    }
suspend fun getAllIdCountriesGenres(): AllCountriesGenresID {
    return retrofit.getAllIdCountriesGenres()
}
}