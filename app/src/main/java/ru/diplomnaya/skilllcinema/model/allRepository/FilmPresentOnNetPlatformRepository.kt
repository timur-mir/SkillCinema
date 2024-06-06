package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.entities.FilmPresentOnNetPlatform
import ru.diplomnaya.skilllcinema.model.entities.Similar
import ru.diplomnaya.skilllcinema.model.network.retrofit

class FilmPresentOnNetPlatformRepository {
    suspend fun getMoreUrlFilmOnNet(idFilm: Int): FilmPresentOnNetPlatform {
        return retrofit.getMoreUrlFilmOnNet(idFilm)
    }
}