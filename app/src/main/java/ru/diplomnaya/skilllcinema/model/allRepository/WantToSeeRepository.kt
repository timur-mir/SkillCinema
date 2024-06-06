package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.database.WantToSeeEntity

class WantToSeeRepository {  private val wantToSeeDao = DataBase.INSTANCE!!.wantToSeeDao()

    suspend fun saveWantToSee(film: WantToSeeEntity)= wantToSeeDao.insertFilm(film)

    fun getWantToSeeFilms()=wantToSeeDao.getAlWantToSeeFilmsBase()

    suspend fun checkFilm(id:Int):Boolean{
        return wantToSeeDao.exists(id)
    }
    suspend fun removeFilmById(id:Int){
        wantToSeeDao.removeWantToSeeFilmById(id)
    }

    fun getWantToSeeFilmsById(id:Int)=wantToSeeDao.getWantToSeeFilmById(id)
    suspend fun getCollectionsSize(): Int {
        return wantToSeeDao.getRowCount()
    }
}
