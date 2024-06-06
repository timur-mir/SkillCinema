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
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.CollectionsFilmsDatabase
import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.database.PremieresEntity
import ru.diplomnaya.skilllcinema.model.database.WantToSeeEntity
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.network.retrofit


class PremieresRepository {

   private val alreadyViewedDao = DataBase.INSTANCE!!.alreadyViewedDao()
    var moviesListCheckedByViewedFilmList: MutableList<Movie> = mutableListOf()
    var mutableListMoviesCopy: MutableList<Movie> = mutableListOf()
    lateinit var viewedFilmList: List<AlreadyViewedEntity>
    private val repoIOScope = CoroutineScope(Dispatchers.IO)
    private var downloader: Job?=null
    suspend fun getPremieres(year: Int, mounth: String): List<Movie> {
        val responseListFilms = retrofit.premieres(year, mounth).items.toMutableList()
        mutableListMoviesCopy = responseListFilms
        loadViewedFilms()
        delay(500)
        for (item in responseListFilms) {
            for (itemDao in viewedFilmList) {
                if (item.kinopoiskId== itemDao.kinopoiskId

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
        return responseListFilms
    }
    private suspend fun loadViewedFilms() {
     downloader?: repoIOScope.launch(Dispatchers.IO) {
         alreadyViewedDao.getAllAlreadyViewedFilmsBase()
                .catch { e ->
                    Log.d("DaoNot", "${e.message}")
                }
                .collect { response ->
                    viewedFilmList = response
                    downloader?.cancel()
                    downloader=null
                }
        }

    }
}

//    suspend fun savePremieresOnNetResponse(year:Int,mounth:String) {
//        val result= retrofit.premieres(year,mounth).items
//        if(result!=null) {
//            premieresDao.insertPremieres(modificationsToRoomPremieres(result))
//        }
// }
//  suspend fun getPremieresOnBase(): Flow<List<PremieresEntity>> = premieresDao.getPremieres()

//    fun modificationsToRoomPremieres(premieres: List<Movie>):List<PremieresEntity> {
//            var mutableList:MutableList<PremieresEntity> = mutableListOf()
//            premieres.forEach {
//                mutableList.add(PremieresEntity(
//                    it.kinopoiskId,it.nameRu,it.posterUrl,it.posterUrlPreview,
//                    it.genres?.joinToString(",") { it.genre.toString() }.toString(),
//                    it.premiereRu,
//                    it.countries?.joinToString(",") { it.country.toString() }.toString(),
//                    it.rating,
//                    it.viewed,
//                    it.filmId
//                ))
//            }
//
//        return mutableList
//    }
//}