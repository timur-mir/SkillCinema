package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.DataBase

class AlreadyViewedRepository {
    private val alreadyViewedDao = DataBase.INSTANCE!!.alreadyViewedDao()

    suspend fun saveAlreadyViewed(film: AlreadyViewedEntity)= alreadyViewedDao.insertFilm(film)

    fun getAllAlreadyViewedFilms()=alreadyViewedDao.getAllAlreadyViewedFilmsBase()

    suspend fun checkFilm(id:Int):Boolean{
        return alreadyViewedDao.exists(id)
    }
    suspend fun removeFilmById(id:Int){
        alreadyViewedDao.removeAlreadyViewedFilmById(id)
    }
    suspend fun removeAllViewedFilms(){
        alreadyViewedDao.removeAllViewedFilms()
    }

    fun getAlreadyViewedFilmsById(id:Int)=alreadyViewedDao.getAlreadyViewedFilmById(id)
}