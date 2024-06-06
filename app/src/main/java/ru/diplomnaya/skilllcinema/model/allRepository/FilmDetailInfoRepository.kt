package ru.diplomnaya.skilllcinema.model.allRepository


import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.network.retrofit


class FilmDetailInfoRepository {
    suspend fun getFilmInfoById(filmId: Int): FilmDetailInfo {
        return retrofit.getFilmById(filmId)
    }


}