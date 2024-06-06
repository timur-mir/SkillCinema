package ru.diplomnaya.skilllcinema.model.allRepository
import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.database.FavouritesEntity
import ru.diplomnaya.skilllcinema.model.database.FavouritesFilmsDao

class FavouritesFilmsRepository {
    private val favouritesDao = DataBase.INSTANCE!!.favouritesDao()

    suspend fun saveFavourite(film: FavouritesEntity)= favouritesDao.insertFilm(film)

   fun getFavouritesFilms()=favouritesDao.getAlFavouritesFilmsBase()

    suspend fun checkFilm(id:Int):Boolean{
       return favouritesDao.exists(id)
    }
    suspend fun removeFilmById(id:Int){
        favouritesDao.removeFavouriteFilmById(id)
    }

fun getFavouritesFilmsById(id:Int)=favouritesDao.getFavouriteFilmById(id)

suspend fun getCollectionsSize(): Int {
   return favouritesDao.getRowCount()
}
}
