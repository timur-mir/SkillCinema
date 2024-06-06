package ru.diplomnaya.skilllcinema.model.allRepository

import kotlinx.coroutines.delay
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.model.network.retrofit

class SerialRepository {
    suspend fun getSerial(iDFilm:Int):Serial{
        return retrofit.getInfoSerial(iDFilm)
    }
}