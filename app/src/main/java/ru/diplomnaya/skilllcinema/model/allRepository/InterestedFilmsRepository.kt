package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity

class InterestedFilmsRepository {
    private val interestedFilmsDao = DataBase.INSTANCE!!.interestedFilmsDao()

    suspend fun saveInterestedFilm(film: InterestedFilmsEntity)= interestedFilmsDao.insertInterestedFilm(film)

    fun getInterestedFilms()=interestedFilmsDao.getInterestedFilmsBase()

    suspend fun checkInterestedFim(idFilm:Int):Boolean{
        return interestedFilmsDao.existsInterestedFilm(idFilm)
    }
    suspend fun removeAllInterestedFilms(){
        interestedFilmsDao.removeAllInterestedFilms()
    }
}