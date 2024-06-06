package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.entities.Images
import ru.diplomnaya.skilllcinema.model.entities.Similar
import ru.diplomnaya.skilllcinema.model.network.retrofit

class SimilarRepository {
    suspend fun getSimilar(idFilm: Int): Similar {
        return retrofit.getSimilarFilms(idFilm)
    }
}